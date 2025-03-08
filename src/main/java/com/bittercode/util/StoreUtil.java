//package com.bittercode.util;
//
//import java.io.PrintWriter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import com.bittercode.model.UserRole;
//
///*
// * Store UTil File To Store Commonly used methods
// */
//public class StoreUtil {
//
//    /**
//     * Check if the User is logged in with the requested role
//     */
//    public static boolean isLoggedIn(UserRole role, HttpSession session) {
//
//        return session.getAttribute(role.toString()) != null;
//    }
//
//    /**
//     * Modify the active tab in the page menu bar
//     */
//    public static void setActiveTab(PrintWriter pw, String activeTab) {
//
//        pw.println("<script>document.getElementById(activeTab).classList.remove(\"active\");activeTab=" + activeTab
//                + "</script>");
//        pw.println("<script>document.getElementById('" + activeTab + "').classList.add(\"active\");</script>");
//
//    }
//
//    /**
//     * Add/Remove/Update Item in the cart using the session
//     */
//    public static void updateCartItems(HttpServletRequest req) {
//        String selectedBookId = req.getParameter("selectedBookId");
//        HttpSession session = req.getSession();
//        if (selectedBookId != null) { // add item to the cart
//
//            // Items will contain comma separated bookIds that needs to be added in the cart
//            String items = (String) session.getAttribute("items");
//            if (req.getParameter("addToCart") != null) { // add to cart
//                if (items == null || items.length() == 0)
//                    items = selectedBookId;
//                else if (!items.contains(selectedBookId))
//                    items = items + "," + selectedBookId; // if items already contains bookId, don't add it
//
//                // set the items in the session to be used later
//                session.setAttribute("items", items);
//
//                /*
//                 * Quantity of each item in the cart will be stored in the session as:
//                 * Prefixed with qty_ following its bookId
//                 * For example 2 no. of book with id 'myBook' in the cart will be
//                 * added to the session as qty_myBook=2
//                 */
//                int itemQty = 0;
//                if (session.getAttribute("qty_" + selectedBookId) != null)
//                    itemQty = (int) session.getAttribute("qty_" + selectedBookId);
//                itemQty += 1;
//                session.setAttribute("qty_" + selectedBookId, itemQty);
//            } else { // remove from the cart
//                int itemQty = 0;
//                if (session.getAttribute("qty_" + selectedBookId) != null)
//                    itemQty = (int) session.getAttribute("qty_" + selectedBookId);
//                if (itemQty > 1) {
//                    itemQty--;
//                    session.setAttribute("qty_" + selectedBookId, itemQty);
//                } else {
//                    session.removeAttribute("qty_" + selectedBookId);
//                    items = items.replace(selectedBookId + ",", "");
//                    items = items.replace("," + selectedBookId, "");
//                    items = items.replace(selectedBookId, "");
//                    session.setAttribute("items", items);
//                }
//            }
//        }
//
//    }
//}


package com.bittercode.util;

import java.io.PrintWriter;
//import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bittercode.model.UserRole;

public class StoreUtil {
    
//    private static final Logger logger = Logger.getLogger(StoreUtil.class.getName());

    public static boolean isLoggedIn(UserRole role, HttpSession session) {
        return session.getAttribute(role.toString()) != null;
    }

    public static void setActiveTab(PrintWriter pw, String activeTab) {
        pw.println("<script>document.getElementById(activeTab).classList.remove(\"active\");activeTab=" + activeTab
                + "</script>");
        pw.println("<script>document.getElementById('" + activeTab + "').classList.add(\"active\");</script>");
    }

    public static void updateCartItems(HttpServletRequest req) {
        String selectedBookId = req.getParameter("selectedBookId");
        HttpSession session = req.getSession();
        if (selectedBookId != null) {
            String items = (String) session.getAttribute("items");
            if (items == null) {
                items = "";
            }

            String addToCart = req.getParameter("addToCart");
            String removeFromCart = req.getParameter("removeFromCart");

//            logger.info("StoreUtil.updateCartItems: addToCart=" + addToCart + 
//                    ", removeFromCart=" + removeFromCart + 
//                    ", selectedBookId=" + selectedBookId + 
//                    ", items(before)=" + items);
            System.out.println("StoreUtil.updateCartItems: addToCart=" + addToCart + 
                    ", removeFromCart=" + removeFromCart + 
                    ", selectedBookId=" + selectedBookId + 
                    ", items(before)=" + items);
            
            if (addToCart != null) { 
                if (!items.contains(selectedBookId)) {
                    items += (items.isEmpty() ? "" : ",") + selectedBookId;
                }
                Integer itemQty = (Integer) session.getAttribute("qty_" + selectedBookId);
                int qty = itemQty != null ? itemQty : 0;
                qty += 1;
                session.setAttribute("qty_" + selectedBookId, qty);
                session.setAttribute("items", items);
                
//                logger.info("Added to cart - BookId: " + selectedBookId + 
//                        ", Items: " + items + 
//                        ", Qty: " + qty);
                System.out.println("Added to cart - BookId: " + selectedBookId + ", Items: " + items + ", Qty: " + qty);
            } else if (removeFromCart != null) { 
                Integer itemQty = (Integer) session.getAttribute("qty_" + selectedBookId);
                int qty = itemQty != null ? itemQty : 0;
                if (qty > 1) {
                    qty--;
                    session.setAttribute("qty_" + selectedBookId, qty);

                    System.out.println("Reduced from cart - BookId: " + selectedBookId + 
                            ", Items: " + items + 
                            ", Qty: " + qty);
//                    logger.info("Reduced from cart - BookId: " + selectedBookId + 
//                            ", Items: " + items + 
//                            ", Qty: " + qty);
                } else {
                    session.removeAttribute("qty_" + selectedBookId);
                    items = items.replace(selectedBookId + ",", "")
                                 .replace("," + selectedBookId, "")
                                 .replace(selectedBookId, "")
                                 .trim();
                    if (items.startsWith(",")) items = items.substring(1);
                    if (items.endsWith(",")) items = items.substring(0, items.length() - 1);
                    session.setAttribute("items", items);
                    
                  System.out.println("Removed from cart - BookId: " + selectedBookId + ", Items: " + items + ", Qty: " + qty);
//                    logger.info("Removed from cart - BookId: " + selectedBookId + 
//                            ", Items: " + items + 
//                            ", Qty: " + qty);
                } 
            } else {
                    
                System.out.println("StoreUtil.updateCartItems: No selectedBookId provided");                
//                logger.warning("StoreUtil.updateCartItems: No selectedBookId provided");
//                }
            }
        }
    }
}
