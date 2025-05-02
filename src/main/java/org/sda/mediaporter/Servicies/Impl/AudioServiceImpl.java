package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.AudioService;
import org.sda.mediaporter.Servicies.FileService;
import org.sda.mediaporter.models.Audio;
import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.repositories.AudioRepository;
import org.sda.mediaporter.repositories.CodecRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRepository;
    private final LanguageRepository languageRepository;
    private final CodecRepository codecRepository;
    private final FileService fileService;

    public AudioServiceImpl(AudioRepository audioRepository, LanguageRepository languageRepository, CodecRepository codecRepository, FileService fileService) {
        this.audioRepository = audioRepository;
        this.languageRepository = languageRepository;
        this.codecRepository = codecRepository;
        this.fileService = fileService;
    }

}
