package org.sda.mediaporter.models.enums;

import lombok.Getter;

@Getter
public enum Subtitles {
    SUBRIP("subrip", ".srt"),
    ASS("ass", ".ass"),
    SSA("ssa", ".ssa"),
    MOV_TEXT("mov_text", ".mp4"),
    WEBVTT("webvtt", ".vtt"),
    DVB_SUBTITLE("dvb_subtitle", ".sub"),
    HDMV_PGS_SUBTITLE("hdmv_pgs_subtitle", ".sup"),
    DVD_SUBTITLE("dvd_subtitle", ".sub"),
    UNKNOWN("unknown", "");

    private final String fileFormat;
    private final String extension;

    Subtitles(String fileFormat, String extension) {
        this.fileFormat = fileFormat;
        this.extension = extension;
    }
}
