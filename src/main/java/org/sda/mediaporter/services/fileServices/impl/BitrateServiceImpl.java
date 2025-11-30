package org.sda.mediaporter.services.fileServices.impl;

import org.sda.mediaporter.services.fileServices.BitrateService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class BitrateServiceImpl implements BitrateService {

    @Override
    public Integer getAudioBitrateFromPathFile(Path pathFile) {
        return getAudioBitrateFromFFMpeg(pathFile);
    }

    @Override
    public Integer getVideoBitrateFromPathFile(Path pathFile) {
        return getVideoBitrateFromFFMpeg(pathFile);
    }

    private Integer getAudioBitrateFromFFMpeg(Path filePath){
        String fFMpegResult = FileServiceImpl.runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "a",
                "-show_entries", "stream=bit_rate",
                "-of", "default=nw=1:nk=1",
                filePath.toString()
        });
        return fFMpegResult.isEmpty()
                ? null
                : Integer.parseInt(fFMpegResult);
    }

    private Integer getVideoBitrateFromFFMpeg(Path filePath){
        String fFMpegResult = FileServiceImpl.runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "v",
                "-show_entries", "stream=bit_rate",
                "-of", "default=nw=1:nk=1",
                filePath.toString()
        });
        return fFMpegResult.isEmpty()
                ? null
                : Integer.parseInt(fFMpegResult);
    }
}
