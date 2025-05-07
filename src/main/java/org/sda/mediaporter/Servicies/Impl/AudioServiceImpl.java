package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.AudioService;
import org.sda.mediaporter.Servicies.CodecService;
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
import java.util.Optional;

@Service
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRepository;
    private final CodecService codecService;

    public AudioServiceImpl(AudioRepository audioRepository, CodecService codecService) {
        this.audioRepository = audioRepository;
        this.codecService = codecService;
    }

    @Override
    public Audio createAudio(Audio audio) {
        return audioRepository.save(audio);
    }
}
