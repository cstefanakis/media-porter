package org.sda.mediaporter.services.fileServices.impl;

import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.*;
import org.sda.mediaporter.repositories.SourcePathRepository;
import org.sda.mediaporter.services.fileServices.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static java.nio.file.Files.delete;

@Service
public class FileServiceImpl implements FileService {

    private final SourcePathRepository sourcePathRepository;

    @Autowired
    public FileServiceImpl(SourcePathRepository sourcePathRepository) {
        this.sourcePathRepository = sourcePathRepository;
    }

    @Override
    public List<Path> getVideoFiles(Path path){
        try{
            return Files.walk(path)
                    .filter(Files::isRegularFile)
                    .filter(file -> isVideoExtension(file.getFileName().toString()))
                    .toList();
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
                createDirectoriesForPath(destinationFilePath);
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
    public void deleteFile(Path path) {
        try {
            if(Files.exists(path)){
                delete(path);
            }
        }catch (IOException e){
            throw new RuntimeException(String.format("Failed to delete %s", path));
        }
    }

    @Override
    public void moveFile(Path fromFullPath, Path toFullPath) {
        if(!Files.exists(toFullPath)){
            try {
                createDirectoriesForPath(toFullPath);
                Files.move(fromFullPath, toFullPath);
                deleteSubDirectories(fromFullPath);
            } catch (IOException e) {
                throw new RuntimeException(String.format("Failed to move file %s to %s", fromFullPath, toFullPath));
            }
        }
    }

    private void createDirectoriesForPath(Path filePath){
        Path path = filePath.getParent();
        if(!Files.exists(path)){
            try{
                Files.createDirectories(filePath.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Path renameFile(Path filePath, String newFileName, String[] newSubdirectories) {
        Path destinationPath = generatedDestinationPathFromFilePath(filePath, newSubdirectories, newFileName);
            try {
                Files.move(filePath, destinationPath);
                deleteSubDirectories(filePath);
            } catch (IOException e) {
                throw new RuntimeException(String.format("Failed to rename file %s to %s", filePath, destinationPath));
            }
        return destinationPath;
    }

    private Path generatedDestinationPathFromFilePath(Path filePath, String[] subdirectories , String newFileName) {
        Path rootPath = getRootPathOfPath(filePath);
        //Add filePath extension to new file name
        String fileNameOfPath = filePath.getFileName().toString();
        newFileName = newFileName.trim() + getFileExtensionWithDot(fileNameOfPath);
        return Path.of(createdDirectories(rootPath, subdirectories) + File.separator + newFileName);
    }

    private Path getRootPathOfPath(Path filePath){
        List<SourcePath> sources = sourcePathRepository.findAll();
        return sources.stream()
                .filter(s-> filePath.normalize().startsWith(Path.of(s.getPath()).normalize()))
                .findFirst()
                .map(s-> Path.of(s.getPath()))
                .orElse(null);
    }

    public void deleteSubDirectories(Path filePath) {
        String rootPath  = getRootPathOfPath(filePath).toString();

        while (!filePath.toString().equals(rootPath)){
            if(Files.exists(filePath) && Files.isDirectory(filePath) && isDirectoryEmpty(filePath)
                || Files.exists(filePath) && Files.isDirectory(filePath)){
                try {
                    Files.delete(filePath);
                } catch (IOException e) {
                    break;
                }
            }
            filePath = filePath.getParent();
        }
    }

    @Override
    public boolean isDirectoryEmpty(Path path) {
        try (var entries = Files.list(path)) {
            return entries.findAny().isEmpty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Path> getAllEmptyDirectories(Path directory) {
        try {
            return Files.walk(directory)
                    .filter(Files::isDirectory)
                    .filter(this::isDirectoryEmpty)
                    .filter(p -> p != directory)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllEmptyDirectories(Path directory) {
        List<Path> emptyDirectories = getAllEmptyDirectories(directory);
        emptyDirectories.forEach(p -> {
            try {
                Files.delete(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void deleteAllDirectoriesWithoutVideFiles(Path rootDirectory) {
        List<Path> directories = new ArrayList<>();
        try {
            directories = Files.walk(rootDirectory)
                    .filter(Files::isDirectory)
                    .filter(p -> p != rootDirectory)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        directories.forEach(p -> {
            if(getVideoFiles(p).isEmpty()){
                deleteAllFilesInDirectory(p);
                try {
                    Files.delete(p);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public List<Path> getVideoFilesUntil(Path directory, LocalDateTime localDateTime) {
        return getVideoFiles(directory).stream()
                .filter(f -> !getModificationLocalDateTimeOfPath(f).isBefore(localDateTime))
                .toList();
    }

    @Override
    public double getFileSizeInMB(Path filePath) {
        try {
            double bytes = Files.size(filePath);
            return bytes / (1024.0 * 1024.0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setLastModifiedTimeToFilePath(Path filePath, LocalDateTime localDateTime) {
        try {
            Files.setLastModifiedTime(
                    filePath,
                    localDateTimeToFileTime(localDateTime)
            );
        } catch (IOException e) {
            throw new RuntimeException("file cant change modification date");
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
    public String getStringWithoutDiacritics(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }

    @Override
    public boolean isFilePathExist(Path filePath) {
        return Files.exists(filePath);
    }

    @Override
    public void deleteAllFilesInDirectory(Path directory) {
        try {
            List<Path> files = Files.walk(directory)
                    .filter(Files::isRegularFile).toList();
            for (Path file : files) {
                Files.delete(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getFileExtensionWithDot(String fileTitle) {
        int dotIndex = fileTitle.lastIndexOf(".");
        if(dotIndex > 0){
            return fileTitle.substring(dotIndex).trim();
        }
        return "";
    }

    @Override
    public String getSafeFileName(String text) {
        return text.replaceAll("[^\\p{L}0-9 ]", " ").replaceAll(" +", " ").trim();
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
