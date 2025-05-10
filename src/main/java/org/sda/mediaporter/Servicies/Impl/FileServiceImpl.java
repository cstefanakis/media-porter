package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.*;
import org.sda.mediaporter.models.enums.*;
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

    public static Integer convertStringToInt(String string){
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(string.trim());
        if(matcher.matches()){
            return Integer.parseInt(matcher.group());
        }return null;
    }

    public static String runCommand(String[] command) {
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
