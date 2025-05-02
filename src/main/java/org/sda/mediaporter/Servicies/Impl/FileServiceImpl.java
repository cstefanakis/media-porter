package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.FileService;
import org.sda.mediaporter.models.Audio;
import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.enums.Extensions;
import org.sda.mediaporter.models.enums.LanguageCodes;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.repositories.AudioRepository;
import org.sda.mediaporter.repositories.CodecRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class FileServiceImpl implements FileService {

    private final LanguageRepository languageRepository;
    private final CodecRepository codecRepository;
    private final AudioRepository audioRepository;

    @Autowired
    public FileServiceImpl(LanguageRepository languageRepository, CodecRepository codecRepository, AudioRepository audioRepository) {
        this.languageRepository = languageRepository;
        this.codecRepository = codecRepository;
        this.audioRepository = audioRepository;
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
    public Codec getVideoCodecFromVideoPath(Path videoPath){
        String codecName = videoCodec(videoPath);
        return videoCodec(codecName);
    }

    //create video codec from string
    private Codec videoCodec(String codecName){
        Optional<Codec> codecOptional = codecRepository.findByName(codecName);
        if (codecOptional.isPresent()){
            return codecOptional.get();
        }else{
            Codec videoCodec = Codec.builder()
                    .name(codecName.trim())
                    .mediaType(MediaTypes.VIDEO)
                    .build();
            return codecRepository.save(videoCodec);
        }
    }

    @Override
    public List<Audio> getAudiosFromPath(Path videoPath){
        List<Audio> audios = new ArrayList<>();
        String audioInfo = audioInfo(videoPath);
        String[] audioInfoArray = audioInfo.split("\n");
        for(String audio : audioInfoArray){
            String[] audioItems = audio.split(",");
            audios.add(audioRepository.save(Audio.builder()
                            .codec(createdCodecFromString(audioItems[1]))
                            .channels(Integer.parseInt(audioItems[2]))
                            .bitrate(Integer.parseInt(audioItems[3])/1000)
                            .language(createdLanguageFromString(audioItems[4]))
                    .build()));
        }return audios;
    }

    private Codec createdCodecFromString(String codecName){
        Optional<Codec> codecOptional = codecRepository.findByName(codecName);
        if (codecOptional.isPresent()){
            return codecOptional.get();
        }else{
            return codecRepository.save(
                    Codec.builder()
                            .name(codecName.trim())
                            .mediaType(MediaTypes.AUDIO)
                            .build()
            );
        }
    }

    private Language createdLanguageFromString(String languageCode){
        Optional <Language> languageOptional = languageRepository.findByCode(languageCode.trim().toLowerCase());
        if (languageOptional.isPresent()){
            return languageOptional.get();
        }else{
            for(LanguageCodes lc : LanguageCodes.values()){
                if(lc.getIso3().equalsIgnoreCase(languageCode.trim()) || lc.getIso2().equalsIgnoreCase(languageCode.trim())){
                    return languageRepository.save(
                            Language.builder()
                                    .englishTitle(lc.getEnglishTitle())
                                    .originalTitle(lc.getOriginalTitle())
                                    .iso2(lc.getIso2())
                                    .iso3(lc.getIso3())
                                    .build()
                    );
                }
            }
        }return null;
    }


    private String audioInfo(Path videoPath){
        String videoFile = videoPath.toString();
        return  runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "a",
                "-show_entries", "stream=index,codec_name,channels,bit_rate:stream_tags=language",
                "-of", "csv=p=0",
                videoFile
        });
    }

    private String videoCodec(Path videoPath){
        String videoFile = videoPath.toString();
        return  runCommand(new String[]{
                "ffprobe", "-v", "error", "-select_streams", "v:0",
                "-show_entries", "stream=codec_name",
                "-of", "default=noprint_wrappers=1:nokey=1",
                videoFile
        });
    }

    private String videoResolution(Path videoPath){
        String videoFile = videoPath.toString();
        return runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=width,height",
                "-of", "csv=s=x:p=0",
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
            System.out.println(output);
            return output.toString();
        }catch (IOException | InterruptedException e){
            throw new RuntimeException("not fount video command");
        }
    }

}
