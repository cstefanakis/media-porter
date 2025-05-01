package org.sda.mediaporter.models.enums;

import lombok.Getter;

@Getter
public enum Extensions {

    //video extensions
    MP4("mp4", MediaTypes.VIDEO),
    MKV("mkv", MediaTypes.VIDEO),
    AVI("avi", MediaTypes.VIDEO),
    MOV("mov", MediaTypes.VIDEO),
    WMV("wmv", MediaTypes.VIDEO),
    FLV("flv", MediaTypes.VIDEO),
    WEBM("webm", MediaTypes.VIDEO),
    MPEG("mpeg", MediaTypes.VIDEO),
    MPG("mpg", MediaTypes.VIDEO),
    M4V("m4v", MediaTypes.VIDEO),
    TS("ts", MediaTypes.VIDEO),
    VOB("vob", MediaTypes.VIDEO);

    //audio extensions

    private final String name;
    private final MediaTypes mediaTypes;

    Extensions(String name, MediaTypes mediaTypes){
        this.name = name;
        this.mediaTypes = mediaTypes;
    }
}
