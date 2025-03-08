package servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.bittercode.constant.BookStoreConstants;
import com.bittercode.constant.ResponseCode;
import com.bittercode.constant.db.BooksDBConstants;
import com.bittercode.model.Book;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
        
public class UpdateBookServlet extends HttpServlet {
    BookService bookService = new BookServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);

        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
        rd.include(req, res);
        StoreUtil.setActiveTab(pw, "storebooks");
        pw.println("<div class='container my-2'>");

        try {
            if (req.getParameter("updateFormSubmitted") != null) {
                String bName = req.getParameter(BooksDBConstants.COLUMN_NAME);
                String bCode = req.getParameter(BooksDBConstants.COLUMN_BARCODE);
                String bAuthor = req.getParameter(BooksDBConstants.COLUMN_AUTHOR);
                double bPrice = Double.parseDouble(req.getParameter(BooksDBConstants.COLUMN_PRICE));
                int bQty = Integer.parseInt(req.getParameter(BooksDBConstants.COLUMN_QUANTITY));

                Part filePart = req.getPart("image");
                String imagePath = null;
                if (filePart != null && filePart.getSize() > 0) {
                    imagePath = saveImage(filePart, bCode);
                } else {
                    Book existingBook = bookService.getBookById(bCode);
                    imagePath = existingBook != null ? existingBook.getImagePath() : null;
                }
                
                Book book = new Book(bCode, bName, bAuthor, bPrice, bQty, imagePath);
                String message = bookService.updateBook(book);
                if (ResponseCode.SUCCESS.name().equalsIgnoreCase(message)) {
                    pw.println(
                            "<table class=\"tab\"><tr><td>Book Detail Updated Successfully!</td></tr></table>");
                } else {
                    pw.println("<table class=\"tab\"><tr><td>Failed to Update Book!!</td></tr></table>");
                    // rd.include(req, res);
                }

                return;
            }

            String bookId = req.getParameter("bookId");

            if (bookId != null) {
                Book book = bookService.getBookById(bookId);
                showUpdateBookForm(pw, book);
            }

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<table class=\"tab\"><tr><td>Failed to Load Book data!!</td></tr></table>");
        }
    }

    private static void showUpdateBookForm(PrintWriter pw, Book book) {
        String form = "<table class=\"tab my-5\" style=\"width:40%;\">\r\n"
                + "        <tr>\r\n"
                + "            <td>\r\n"
                + "                <form action=\"updatebook\" method=\"post\" enctype=\"multipart/form-data\">\r\n"
                + "                    <label for=\"bookCode\">Book Code : </label><input type=\"text\" name=\"barcode\" id=\"bookCode\" placeholder=\"Enter Book Code\" value='"
                + book.getBarcode() + "' readonly><br/>"
                + "                    <label for=\"bookName\">Book Name : </label> <input type=\"text\" name=\"name\" id=\"bookName\" placeholder=\"Enter Book's name\" value='"
                + book.getName() + "' required><br/>\r\n"
                + "                    <label for=\"bookAuthor\">Book Author : </label><input type=\"text\" name=\"author\" id=\"bookAuthor\" placeholder=\"Enter Author's Name\" value='"
                + book.getAuthor() + "' required><br/>\r\n"
                + "                    <label for=\"bookPrice\">Book Price : </label><input type=\"number\" name=\"price\" placeholder=\"Enter the Price\" value='"
                + book.getPrice() + "' required><br/>\r\n"
                + "                    <label for=\"bookQuantity\">Book Qnty : </label><input type=\"number\" name=\"quantity\" id=\"bookQuantity\" placeholder=\"Enter the quantity\" value='"
                + book.getQuantity() + "' required><br/>\r\n"
                + "                    <label for=\"bookImage\">Book Image : </label><input type=\"file\" name=\"image\" id=\"bookImage\" accept=\"image/*\"><br/>\r\n"
                + "                    <input class=\"btn btn-success my-2\" type=\"submit\" name='updateFormSubmitted' value=\" Update Book \">\r\n"
                + "                </form>\r\n"
                + "            </td>\r\n"
                + "        </tr>  \r\n"
                + "    </table>";
        pw.println(form);
    }
    
    private String saveImage(Part filePart, String bookCode) throws IOException, ServletException {
        String uploadPath = getServletContext().getRealPath("/images");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            throw new ServletException("Failed to create upload directory: " + uploadPath);
        }

        String fileName = bookCode + "_" + filePart.getSubmittedFileName();
        String filePath = uploadPath + File.separator + fileName;
        System.out.println("Saving image to: " + filePath);
        filePart.write(filePath);
        return "images/" + fileName;
    }
}
