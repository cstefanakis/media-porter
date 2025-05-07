package org.sda.mediaporter.Servicies.Impl;

import org.hibernate.engine.spi.Resolution;
import org.sda.mediaporter.Servicies.*;
import org.sda.mediaporter.models.Audio;
import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.enums.*;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.AudioRepository;
import org.sda.mediaporter.repositories.CodecRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements FileService {

    private final LanguageService languageService;
    private final CodecService codecService;
    private final AudioService audioService;
    private final VideoRepository videoRepository;
    private final VideoService videoService;

    @Autowired
    public FileServiceImpl(LanguageService languageService, CodecService codecService, AudioService audioService, VideoRepository videoRepository, VideoService videoService) {
        this.languageService = languageService;
        this.codecService = codecService;
        this.audioService = audioService;
        this.videoRepository = videoRepository;
        this.videoService = videoService;
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
    @Override
    public Video getVideoInfoFromPath(Path videoPath){
        String[] properties = videoInfo(videoPath).split(",");
        System.out.println(properties[4]);
        return videoService.createVideo(
                Video.builder()
                        .codec(codecService.autoCreateCodec(properties[1]))
                        .resolution(generatedResolution(properties[2], properties[3]))
                        .bitrate(convertStringToInt(properties[4]))
                        .build()
        );
    }

    private String generatedResolution(String widthStr, String heightStr){
        Integer width = convertStringToInt(widthStr);
        Integer height = convertStringToInt(heightStr);
        for(Resolutions resolution : Resolutions.values()){
            if(width !=null &&
                    resolution.getWidth() == width && height != null &&
                    resolution.getHeight() == height ){
                return resolution.getLabel();
            }

            if(width != null && resolution.getWidth() == width){
                return resolution.getLabel();
            }
        }return null;
    }

    private Integer convertStringToInt(String string){
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(string.trim());
        if(matcher.matches()){
            return Integer.parseInt(matcher.group());
        }return null;
    }

    @Override
    public List<Audio> getAudiosInfoFromPath(Path videoPath){
        List<Audio> audios = new ArrayList<>();
        String audioInfo = audioInfo(videoPath);
        String[] audioInfoArray = audioInfo.split("\n");
        for(String audio : audioInfoArray){
            String[] audioItems = audio.split(",");
            Audio createdAudio = audioService.createAudio(Audio.builder()
                    .codec(codecService.autoCreateCodec(audioItems[1]))
                    .channels(Integer.parseInt(audioItems[2]))
                    .bitrate(convertStringToInt(audioItems[3]))
                    .language(languageService.autoCreateLanguageByCode(audioItems[4]))
                    .build());
            audios.add(createdAudio);
        }
        return audios;
    }

    public List<Subtitle> getSubtitlesInfoFromPath(Path filePath){
        List<Subtitle> subtitlesList = new ArrayList<>();
        String[] subtitles = subtitleInfo(filePath).split("\n");
        if(subtitles.length >= 1) {
            for (String subtitle : subtitles) {
                String[] properties = subtitle.split(",");

                if(properties.length >= 3) {
                    subtitlesList.add(Subtitle.builder()
                            .language(languageService.autoCreateLanguageByCode(properties[2]))
                            .format(codecService.autoCreateCodec(properties[1]))
                            .build());
                }
            }
        }return subtitlesList;
    }

    private String audioInfo(Path filePath){
        return  runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "a",
                "-show_entries", "stream=index,codec_name,channels,bit_rate:stream_tags=language",
                "-of", "csv=p=0",
                filePath.toString()
        });
    }

    private String videoInfo(Path filePath){
        return  runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=index,codec_name,width,height,bit_rate",
                "-of", "csv=p=0",
                filePath.toString()
        });
    }

    private String subtitleInfo(Path filePath){
        return runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "s",
                "-show_entries", "stream=index,codec_name:stream_tags=language",
                "-of", "csv=p=0",
                filePath.toString()
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
            System.out.println(output);
            return output.toString();
        }catch (IOException | InterruptedException e){
            throw new RuntimeException("not fount video command");
        }
    }

}
