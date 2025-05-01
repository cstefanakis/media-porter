package org.sda.mediaporter.models.enums;

import lombok.Getter;

@Getter
public enum LanguageCodes {
    ENGLISH("en", "eng"),
    SPANISH("es", "spa"),
    FRENCH("fr", "fra"),
    GERMAN("de", "deu"),
    ITALIAN("it", "ita"),
    PORTUGUESE("pt", "por"),
    RUSSIAN("ru", "rus"),
    CHINESE("zh", "zho"),
    JAPANESE("ja", "jpn"),
    KOREAN("ko", "kor"),
    ARABIC("ar", "ara"),
    HINDI("hi", "hin"),
    TURKISH("tr", "tur"),
    POLISH("pl", "pol"),
    DUTCH("nl", "nld"),
    SWEDISH("sv", "swe"),
    FINNISH("fi", "fin"),
    NORWEGIAN("no", "nor"),
    DANISH("da", "dan"),
    GREEK("el", "ell"),
    CZECH("cs", "ces"),
    ROMANIAN("ro", "ron"),
    HUNGARIAN("hu", "hun"),
    THAI("th", "tha"),
    INDONESIAN("id", "ind"),
    HEBREW("he", "heb"),
    UKRAINIAN("uk", "ukr"),
    VIETNAMESE("vi", "vie"),
    MALAY("ms", "msa"),
    PERSIAN("fa", "fas"),
    BENGALI("bn", "ben");

    private final String iso2;
    private final String iso3;

    LanguageCodes(String iso2, String iso3) {
        this.iso2 = iso2;
        this.iso3 = iso3;
    }
}
