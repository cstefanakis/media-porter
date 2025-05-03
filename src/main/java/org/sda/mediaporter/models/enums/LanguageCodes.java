package org.sda.mediaporter.models.enums;

import lombok.Getter;

@Getter
public enum LanguageCodes {
    ENGLISH("en", "eng", "eng", "English", "English"),
    SPANISH("es", "spa", "spa", "Spanish", "Español"),
    FRENCH("fr", "fre", "fra", "French", "Français"),
    GERMAN("de", "ger", "deu", "German", "Deutsch"),
    ITALIAN("it", "ita", "ita", "Italian", "Italiano"),
    PORTUGUESE("pt", "por", "por", "Portuguese", "Português"),
    RUSSIAN("ru", "rus", "rus", "Russian", "Русский"),
    CHINESE("zh", "chi", "zho", "Chinese", "中文"),
    JAPANESE("ja", "jpn", "jpn", "Japanese", "日本語"),
    KOREAN("ko", "kor", "kor", "Korean", "한국어"),
    ARABIC("ar", "ara", "ara", "Arabic", "العربية"),
    HINDI("hi", "hin", "hin", "Hindi", "हिन्दी"),
    TURKISH("tr", "tur", "tur", "Turkish", "Türkçe"),
    POLISH("pl", "pol", "pol", "Polish", "Polski"),
    DUTCH("nl", "dut", "nld", "Dutch", "Nederlands"),
    SWEDISH("sv", "swe", "swe", "Swedish", "Svenska"),
    FINNISH("fi", "fin", "fin", "Finnish", "Suomi"),
    NORWEGIAN("no", "nor", "nor", "Norwegian", "Norsk"),
    DANISH("da", "dan", "dan", "Danish", "Dansk"),
    GREEK("el", "gre", "ell", "Greek", "Ελληνικά"),
    CZECH("cs", "cze", "ces", "Czech", "Čeština"),
    ROMANIAN("ro", "rum", "ron", "Romanian", "Română"),
    HUNGARIAN("hu", "hun", "hun", "Hungarian", "Magyar"),
    THAI("th", "tha", "tha", "Thai", "ไทย"),
    INDONESIAN("id", "ind", "ind", "Indonesian", "Bahasa Indonesia"),
    HEBREW("he", "heb", "heb", "Hebrew", "עברית"),
    UKRAINIAN("uk", "ukr", "ukr", "Ukrainian", "Українська"),
    VIETNAMESE("vi", "vie", "vie", "Vietnamese", "Tiếng Việt"),
    MALAY("ms", "may", "msa", "Malay", "Bahasa Melayu"),
    PERSIAN("fa", "per", "fas", "Persian", "فارسی"),
    BENGALI("bn", "ben", "ben", "Bengali", "বাংলা"),
    SLOVAK("sk", "slo", "slk", "Slovak", "Slovenčina");


    private final String iso6391;
    private final String iso6392B;
    private final String iso6392T;
    private final String englishTitle;
    private final String originalTitle;

    LanguageCodes(String iso6391, String iso6392B, String iso6392T, String englishTitle, String originalTitle) {
        this.iso6391 = iso6391;
        this.iso6392B = iso6392B;
        this.iso6392T = iso6392T;
        this.englishTitle = englishTitle;
        this.originalTitle = originalTitle;
    }
}
