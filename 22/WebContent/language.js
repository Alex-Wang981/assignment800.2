// Define translations for English and Māori
const translations = {
    en: {
        // Navigation
        "title": "Book Store",
        "nav-home": "Home",
        "nav-login": "Login",
        "nav-register": "Register",
        "nav-language": "Language",
        
        // Main content
        "welcome": "Magic Online Book Store",
        "slogan": "Discover a world of knowledge and inspiration.",
        "explore": "Start Exploring",
        
        // About section
        "about-title": "About Our Book Store",
        "about-text-1": "Welcome to Magic Online Book Store, your ultimate destination for books of all genres-fiction, non-fiction, fantasy, science, and more. We offer a curated selection of both classic and contemporary titles, ensuring every reader finds something inspiring. Our platform is designed to provide a seamless shopping experience, with fast delivery, digital downloads, and exclusive offers for our loyal customers.",
        "about-text-2": "Whether you're a student, a professional, or a casual reader, we have books to enrich your mind and spark your imagination. Explore our collections, join our community, and enjoy special promotions, including monthly book clubs and author events. Happy learning and reading!",
        
        // Categories section
        "categories-title": "Our Featured Categories",
        "category-fantasy": "Fantasy & Magic",
        "category-fantasy-desc": "Dive into enchanting worlds of wizards, dragons, and mythical creatures.",
        "category-science": "Science & Technology",
        "category-science-desc": "Explore the latest in science, innovation, and technological advancements.",
        "category-classics": "Classics & Literature",
        "category-classics-desc": "Rediscover timeless classics and literary masterpieces.",
        
        // Contact section
        "contact-title": "Contact Us",
        "contact-text": "Have questions? Reach out to us at support@magiconlinestore.com or call us at (123) 456-7890. We're here to help you find your next great read!"
    },
    mi: {
        // Navigation
        "title": "Toa Pukapuka",
        "nav-home": "Kāinga",
        "nav-login": "Takiuru",
        "nav-register": "Rēhita",
        "nav-language": "Reo",
        
        // Main content
        "welcome": "Toa Pukapuka Matihiko Makutu",
        "slogan": "Tūhuratia tētahi ao mātauranga me te whakaihiihi.",
        "explore": "Tīmata Tūhura",
        
        // About section
        "about-title": "Mō Tō Mātou Toa Pukapuka",
        "about-text-1": "Nau mai ki te Toa Pukapuka Matihiko Makutu, tō wāhi tino pai mō ngā pukapuka o ngā momo katoa - paki, pono, pūrākau, pūtaiao, me ētahi atu. Ka tuku mātou i tētahi kohinga o ngā taitara tawhito me ngā taitara o nāianei, kia mōhio ai ia kaipānui he mea whakahihiri. Kua hangaia tā mātou papaaho kia ngāwari ai te wheako hokohoko, me te tuku tere, te tango matihiko, me ngā tuku motuhake mō ā mātou kiritaki pono.",
        "about-text-2": "Ahakoa he ākonga koe, he tohunga, he kaipānui noa rānei, he pukapuka ā mātou hei whakapai i tō hinengaro, hei whakaihiihi i tō pūmanawa auaha. Tūhurahia ā mātou kohinga, honoa ki tō mātou hapori, ā, ka pai ki a koe ngā whakatairanga motuhake, tae atu ki ngā karapu pukapuka ā-marama me ngā huihuinga kaituhi. Kia hari te ako me te pānui!",
        
        // Categories section
        "categories-title": "Ko Ā Mātou Kāwai Tino Pai",
        "category-fantasy": "Pūrākau & Makutu",
        "category-fantasy-desc": "Rukuhia ki ngā ao whakangao o ngā tohunga makutu, ngā taniwha, me ngā kararehe pūrākau.",
        "category-science": "Pūtaiao & Hangarau",
        "category-science-desc": "Tūhurahia ngā mea hou i roto i te pūtaiao, te auahatanga, me ngā whanaketanga hangarau.",
        "category-classics": "Tūturu & Tuhinga",
        "category-classics-desc": "Kitea anō ngā tuhinga tūturu me ngā tuhinga tino pai.",
        
        // Contact section
        "contact-title": "Whakapā Mai",
        "contact-text": "He pātai āu? Toro mai ki a mātou ki te support@magiconlinestore.com, waea mai rānei ki a mātou ki te (123) 456-7890. Kei konei mātou hei āwhina i a koe ki te kimi i tō pānuitanga nui!"
    }
};

// Get the browser language or use a default
let currentLanguage = localStorage.getItem('language') || 'en';

// Function to change the language
function changeLanguage(language) {
    currentLanguage = language;
    translatePage();
    
    // Save language preference to localStorage
    localStorage.setItem('language', language);
}

// Function to translate the page
function translatePage() {
    const elements = document.querySelectorAll('[data-i18n]');
    
    elements.forEach(element => {
        const key = element.getAttribute('data-i18n');
        
        if (translations[currentLanguage][key]) {
            // Special handling for elements that might contain HTML
            if (key === 'contact-text') {
                // Preserve the email link
                const emailRegex = /<a href="mailto:[^"]+">([^<]+)<\/a>/;
                const emailMatch = translations['en'][key].match(emailRegex);
                
                if (emailMatch) {
                    let translatedText = translations[currentLanguage][key];
                    // If the translation doesn't already have the email link, insert it
                    if (!translatedText.includes('<a href="mailto:')) {
                        translatedText = translatedText.replace(
                            'support@magiconlinestore.com', 
                            `<a href="mailto:support@magiconlinestore.com">support@magiconlinestore.com</a>`
                        );
                    }
                    element.innerHTML = translatedText;
                } else {
                    element.textContent = translations[currentLanguage][key];
                }
            } else {
                // Standard text replacement
                element.textContent = translations[currentLanguage][key];
            }
        }
    });
    
    // Update page title
    document.title = translations[currentLanguage]['title'];
}

// Initialize the page with the stored language preference
document.addEventListener('DOMContentLoaded', () => {
    translatePage();
});