package org.sda.mediaporter.models.enums;

import lombok.Getter;

@Getter
public enum Resolutions {
    SD_480P("480p", 720, 480, "4:3"),
    SD_576P("576p", 720, 576, "5:4"),
    HD_720P("720p", 1280, 720, "16:9"),
    HD_900P("900p", 1600, 900, "16:9"),
    FULL_HD_1080P("1080p", 1920, 1080, "16:9"),
    QHD_1440P("1440p", 2560, 1440, "16:9"),
    UHD_4K("4K", 3840, 2160, "16:9"),
    UHD_5K("5K", 5120, 2880, "16:9"),
    UHD_8K("8K", 7680, 4320, "16:9"),
    WXGA("WXGA", 1280, 800, "16:10"),
    SXGA("SXGA", 1280, 1024, "5:4"),
    XGA_PLUS("XGA+", 1280, 960, "4:3");

    private final String label;
    private final int width;
    private final int height;
    private final String aspectRatio;

    Resolutions(String label, int width, int height, String aspectRatio) {
        this.label = label;
        this.width = width;
        this.height = height;
        this.aspectRatio = aspectRatio;
    }
}
