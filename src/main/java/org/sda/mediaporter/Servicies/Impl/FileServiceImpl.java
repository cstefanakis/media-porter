package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.FileService;
import org.sda.mediaporter.Servicies.LanguageService;
import org.sda.mediaporter.api.LanguageApi;
import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.enums.Codecs;
import org.sda.mediaporter.models.enums.Extensions;
import org.sda.mediaporter.models.enums.LanguageCodes;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements FileService {

    private final LanguageService languageService;
    private final LanguageRepository languageRepository;

    public FileServiceImpl(LanguageService languageService, LanguageRepository languageRepository) {
        this.languageService = languageService;
        this.languageRepository = languageRepository;
    }

    @Override
    public List<Path> getVideoFiles(Path path){
        return getVideoFilesOfSource(path);
    }


    @Override
    public void copyFile(Path fromFullPath, Path toFullPath) {

            if(Files.exists(toFullPath) && isSameSizeBetweenTowFiles(fromFullPath,toFullPath)){
                try {
                    Files.copy(fromFullPath, toFullPath);
                } catch (IOException e) {
                    throw new RuntimeException(String.format("Failed to copy file %s to %s", fromFullPath, toFullPath));
                }
            }
            if(!Files.exists(toFullPath)){
                try {
                    Files.copy(fromFullPath, toFullPath);
                } catch (IOException e) {
                    throw new RuntimeException(String.format("Failed to copy file %s to %s", fromFullPath, toFullPath));
                }
            }
    }

    private boolean isSameSizeBetweenTowFiles(Path file1, Path file2)  {
        try {
            if (Files.size(file1) ==  Files.size(file2)){
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    @Override
    public void deleteFile(Path path) {
        try {
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException(String.format("Failed to delete %s", path));
        }
    }

    @Override
    public void moveFile(Path fromFullPath, Path toFullPath) {
        if(!Files.exists(toFullPath)){
            try {
                Files.copy(fromFullPath, toFullPath);
            } catch (IOException e) {
                throw new RuntimeException(String.format("Failed to move file %s to %s", fromFullPath, toFullPath));
            }
        }
    }

    @Override
    public void renameFile(Path filePath, String newName, Integer year) {
        String newNameWithExtension = newName.trim() + getFileExtensionWithDot(filePath);
            try {
                Files.move(filePath, renamedPath(filePath, newName, year));
            } catch (IOException e) {
                throw new RuntimeException(String.format("Failed to rename file %s to %s", filePath.getFileName(), newNameWithExtension));
            }
    }

    @Override
    public Path renamedPath(Path filePath, String newName, Integer year){
        String newNameWithExtension = newName.trim() + getFileExtensionWithDot(filePath);
        return filePath.resolveSibling(String.format("%s (%s)%s", newName, year, newNameWithExtension));
    }

    @Override
    public String getFileExtensionWithDot(Path file) {
        int dotIndex = file.getFileName().toString().lastIndexOf(".");
        if(dotIndex > 0){
            return file.getFileName().toString().substring(dotIndex);
        }
        return "";
    }

    //scan files
    @Override
    public List<Path> getVideoFilesOfSource(Path path){
        try{
            return Files.walk(path).filter(file -> isVideoExtension(file.getFileName().toString())).toList();
        }catch (IOException e){
            return List.of();
        }
    }

    //check if fileName is video type
    private boolean isVideoExtension(String fileName){
        for(Extensions extension : Extensions.values()){
            if(fileName.toLowerCase().endsWith(extension.getName()) && extension.getMediaTypes().equals(MediaTypes.VIDEO)){
                return true;
            }
        }return false;
    }

    //get video codec from video file
    public String getVideoCodecFromVideoPath(Path videoPath){
        String videoFile = videoPath.toString();
        return runCommand(new String[]{
                "ffprobe", "-v", "error", "-select_streams", "v:0",
                "-show_entries", "stream=codec_name",
                "-of", "default=noprint_wrappers=1:nokey=1",
                videoFile});
    }

    //get audio codec from video file
    @Override
    public Map<Codec, Language> getAudiosCodecAndLanguageFromAudioPath(Path videoPath) {
        Map<Codec, Language> audioCodecAndLanguage = new HashMap<>();
        String audio = getVideoCodecFromVideoPath(videoPath);
        String[] audios = audio.split("index=");
        for (String a : audios) {
            Codec codec = getGenerateCodecFromString(a);
            Language language = getGenerateLanguageFromString(a);
            audioCodecAndLanguage.put(codec, language);
        }return audioCodecAndLanguage;
    }

    //get language from String
    private Language getGenerateLanguageFromString(String ffmpeg){
        for (LanguageCodes l : LanguageCodes.values()) {
            if (ffmpeg.toLowerCase().contains(l.getIso3().toLowerCase())) {
                Optional<Language> languageOptional = languageRepository.findByCode(l.getIso2().toLowerCase());
                if (languageOptional.isPresent()) {
                    return languageOptional.get();

                } else {
                    LanguageApi languageApi = new LanguageApi();
                    List<Language> languages = languageApi.getLanguages();
                    for (Language lang : languages) {
                        if (lang.getCode().equalsIgnoreCase(l.getIso2().toLowerCase())) {
                            return languageRepository.save(lang);
                        }
                    }
                }
            }
        }return null;
    }

    //get codec from String
    private Codec getGenerateCodecFromString(String ffmpeg){
        for (Codecs c : Codecs.values()) {
            if (ffmpeg.toLowerCase().contains(c.name().toLowerCase())) {
                return Codec.builder()
                        .name(c.name())
                        .mediaType(MediaTypes.AUDIO)
                        .build();
            }
        }return null;
    }


    private String audioInfo(Path videoPath){
        String videoFile = videoPath.toString();
        return  runCommand(new String[]{
                "ffprobe", "-v", "error", "-select_streams", "a",
                "-show_entries", "stream=codec_name:stream_tags=language",
                "-of", "default=noprint_wrappers=1",
                videoFile
        });
    }

    private String runCommand(String[] command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        try {
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            return output.toString();
        }catch (IOException | InterruptedException e){
            throw new RuntimeException("not fount video command");
        }
    }

}
