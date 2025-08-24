package org.sda.mediaporter.Services.Impl;

import org.sda.mediaporter.Services.*;
import org.sda.mediaporter.models.enums.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements FileService {

    public FileServiceImpl() {
    }

    @Override
    public List<Path> getVideoFiles(Path path){
        try{
            return Files.walk(path).filter(file -> isVideoExtension(file.getFileName().toString())).toList();
        }catch (IOException e){
            return List.of();
        }
    }


    @Override
    public void copyFile(Path filePath, Path destinationFilePath) {
        Path prepareFullPath = destinationFilePath.resolveSibling(destinationFilePath.getFileName() + ".copy");
        if((Files.exists(destinationFilePath) && !isSameSizeBetweenTowFiles(filePath,destinationFilePath)) ||
                !Files.exists(destinationFilePath)){
            try {
                Files.copy(filePath, prepareFullPath);
                FileTime now = localDateTimeToFileTime(LocalDateTime.now());
                Files.setLastModifiedTime(prepareFullPath, now);
                Files.move(prepareFullPath, destinationFilePath);
            } catch (IOException e) {
                throw new RuntimeException(String.format("Failed to copy file %s to %s", filePath, destinationFilePath));
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
    public void deleteFile(Path path, String fileNameContain) {
        try {
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException(String.format("Failed to delete %s", path));
        }
        deleteSubDirectories(path, fileNameContain);
    }

    @Override
    public void moveFile(Path fromFullPath, String filenameTitleAndYear, Path toFullPath) {
        if(!Files.exists(toFullPath)){
            try {
                Files.move(fromFullPath, toFullPath);
                deleteSubDirectories(fromFullPath, filenameTitleAndYear);
            } catch (IOException e) {
                throw new RuntimeException(String.format("Failed to move file %s to %s", fromFullPath, toFullPath));
            }
        }
    }

    @Override
    public Path renameFile(Path filePath,String oldSubDirectoryName, String newName) {
        String newNameWithExtension = newName.trim() + getFileExtensionWithDot(filePath);
        Path destinationPath = generatedDestinationPathFromFilePath(filePath, oldSubDirectoryName, newNameWithExtension);
            try {
                Files.move(filePath, destinationPath);
            } catch (IOException e) {
                throw new RuntimeException(String.format("Failed to rename file %s to %s", filePath, destinationPath));
            }
        deleteSubDirectories(filePath, oldSubDirectoryName);
        return destinationPath;
    }

    public Path generatedDestinationPathFromFilePath(Path filePath, String fileNameContain, String nameWithExtension) {
        while(filePath.toString().contains(fileNameContain)){
            filePath = filePath.getParent();
        }
        return filePath.resolve(nameWithExtension);
    }

    public void deleteSubDirectories(Path filePath, String fileNameContain) {
        while(filePath.toString().contains(fileNameContain)){
            File path = filePath.toFile();
            System.out.println(filePath);
            if(path.isDirectory() && path.listFiles() != null && path.listFiles().length == 0){
                try {
                    Files.delete(filePath);
                }catch (IOException e){
                    throw new RuntimeException(String.format("Failed to delete directory %s", filePath));
                }
            }
            filePath = filePath.getParent();

        }
    }

    @Override
    public LocalDateTime getModificationLocalDateTimeOfPath(Path path) {
        try {
            FileTime fileTime = Files.getLastModifiedTime(path);
            Instant instant = fileTime.toInstant();
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileTime localDateTimeToFileTime(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return FileTime.from(zonedDateTime.toInstant());
    }


    @Override
    public String getFileExtensionWithDot(Path file) {
        int dotIndex = file.getFileName().toString().lastIndexOf(".");
        if(dotIndex > 0){
            return file.getFileName().toString().substring(dotIndex);
        }
        return "";
    }

    @Override
    public Path createdDirectories(Path sourcePath, String[] directories) {
        StringBuilder path = new StringBuilder();
        path.append(sourcePath.toString());
        for(int i = 0; i < directories.length; i++){
            path.append(File.separator).append(directories[i]);
            if(i < directories.length - 1){
                path.append(File.separator);
            }
            Path directoryPath = Path.of(path.toString());
            if(!Files.exists(directoryPath)){
                try {
                    Files.createDirectory(directoryPath);
                }catch (IOException e){
                    throw new RuntimeException(String.format("Failed to create directory %s", path));
                }
            }
        }return Path.of(path.toString());
    }

    //check if fileName is video type
    private boolean isVideoExtension(String fileName){
        for(Extensions extension : Extensions.values()){
            if(fileName.toLowerCase().endsWith(extension.getName()) && extension.getMediaTypes().equals(MediaTypes.VIDEO)){
                return true;
            }
        }return false;
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
