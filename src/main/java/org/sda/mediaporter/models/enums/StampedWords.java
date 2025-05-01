package org.sda.mediaporter.models.enums;

import lombok.Getter;

@Getter
public enum StampedWords {
    DABING("dabing"),
    CZDAB("czdab"),
    GENRES("genres");

    private final String name;

    StampedWords(String name) {
        this.name = name;
    }
}
