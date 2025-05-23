package org.sda.mediaporter.models.enums;

import lombok.Getter;

@Getter
public enum Codecs {
    // Video codecs
    H264("H.264", MediaTypes.VIDEO),
    X264("x264", MediaTypes.VIDEO),
    AVC("AVC", MediaTypes.VIDEO),
    H265("H.265", MediaTypes.VIDEO),
    AV1("AV1", MediaTypes.VIDEO),
    VP9("VP9", MediaTypes.VIDEO),
    VP8("VP8", MediaTypes.VIDEO),
    MPEG2("MPEG-2", MediaTypes.VIDEO),
    MPEG4("MPEG-4", MediaTypes.VIDEO),
    XVID("Xvid", MediaTypes.VIDEO),
    DIVX("DivX", MediaTypes.VIDEO),

    // Audio codecs
    EAC3("EAC3", MediaTypes.AUDIO),
    AAC("AAC", MediaTypes.AUDIO),
    MP3("MP3", MediaTypes.AUDIO),
    FLAC("FLAC", MediaTypes.AUDIO),
    ALAC("ALAC", MediaTypes.AUDIO),
    OPUS("Opus", MediaTypes.AUDIO),
    VORBIS("Vorbis", MediaTypes.AUDIO),
    PCM("PCM", MediaTypes.AUDIO),
    WMA("WMA", MediaTypes.AUDIO),
    AC3("AC-3", MediaTypes.AUDIO),
    AC3_ALT("AC3", MediaTypes.AUDIO),
    DTS("DTS", MediaTypes.AUDIO),

    //Subtitles
    SUBRIP("subrip", MediaTypes.SUBTITLE),
    ASS("ass", MediaTypes.SUBTITLE),
    SSA("ssa", MediaTypes.SUBTITLE),
    MOV_TEXT("mov_text", MediaTypes.SUBTITLE),
    WEBVTT("webvtt", MediaTypes.SUBTITLE),
    DVB_SUBTITLE("dvb_subtitle", MediaTypes.SUBTITLE),
    HDMV_PGS_SUBTITLE("hdmv_pgs_subtitle", MediaTypes.SUBTITLE),
    DVD_SUBTITLE("dvd_subtitle", MediaTypes.SUBTITLE);

    private final String codecName;
    private final MediaTypes mediaTypes;

    Codecs(String codecName, MediaTypes mediaTypes) {
        this.codecName = codecName;
        this.mediaTypes = mediaTypes;
    }

}
