package org.sda.mediaporter.models.enums;

import lombok.Getter;

@Getter
public enum LanguageCodes {
    ENGLISH("en", "eng", "English", "English"),
    SPANISH("es", "spa", "Spanish", "Español"),
    FRENCH("fr", "fra", "French", "Français"),
    GERMAN("de", "deu", "German", "Deutsch"),
    ITALIAN("it", "ita", "Italian", "Italiano"),
    PORTUGUESE("pt", "por", "Portuguese", "Português"),
    RUSSIAN("ru", "rus", "Russian", "Русский"),
    CHINESE("zh", "zho", "Chinese", "中文"),
    JAPANESE("ja", "jpn", "Japanese", "日本語"),
    KOREAN("ko", "kor", "Korean", "한국어"),
    ARABIC("ar", "ara", "Arabic", "العربية"),
    HINDI("hi", "hin", "Hindi", "हिन्दी"),
    TURKISH("tr", "tur", "Turkish", "Türkçe"),
    POLISH("pl", "pol", "Polish", "Polski"),
    DUTCH("nl", "nld", "Dutch", "Nederlands"),
    SWEDISH("sv", "swe", "Swedish", "Svenska"),
    FINNISH("fi", "fin", "Finnish", "Suomi"),
    NORWEGIAN("no", "nor", "Norwegian", "Norsk"),
    DANISH("da", "dan", "Danish", "Dansk"),
    GREEK("el", "ell", "Greek", "Ελληνικά"),
    CZECH("cs", "ces", "Czech", "Čeština"),
    ROMANIAN("ro", "ron", "Romanian", "Română"),
    HUNGARIAN("hu", "hun", "Hungarian", "Magyar"),
    THAI("th", "tha", "Thai", "ไทย"),
    INDONESIAN("id", "ind", "Indonesian", "Bahasa Indonesia"),
    HEBREW("he", "heb", "Hebrew", "עברית"),
    UKRAINIAN("uk", "ukr", "Ukrainian", "Українська"),
    VIETNAMESE("vi", "vie", "Vietnamese", "Tiếng Việt"),
    MALAY("ms", "msa", "Malay", "Bahasa Melayu"),
    PERSIAN("fa", "fas", "Persian", "فارسی"),
    BENGALI("bn", "ben", "Bengali", "বাংলা");

    private final String iso2;
    private final String iso3;
    private final String englishTitle;
    private final String originalTitle;

    LanguageCodes(String iso2, String iso3,String englishTitle, String originalTitle) {
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.englishTitle = englishTitle;
        this.originalTitle = originalTitle;
    }
}
