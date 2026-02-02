package org.sda.mediaporter.services.audioServices;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.AudioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AudioServiceTest {

    @Autowired
    private AudioService audioService;
}