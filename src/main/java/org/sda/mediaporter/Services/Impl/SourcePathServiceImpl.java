package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.SourcePathService;
import org.sda.mediaporter.dtos.SourcePathDto;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.repositories.SourcePathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SourcePathServiceImpl implements SourcePathService {

    private final SourcePathRepository sourcePathRepository;

    @Autowired
    public SourcePathServiceImpl(SourcePathRepository sourcePathRepository) {
        this.sourcePathRepository = sourcePathRepository;
    }

    @Override
    public SourcePath getById(Long id) {
        return sourcePathRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Source path with id %s not found", id)));
    }

    @Override
    public List<SourcePath> getSourcePaths() {
        return sourcePathRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        sourcePathRepository.delete(getById(id));
    }

    @Override
    public SourcePath createSourcePath(SourcePathDto sourcePathDto) {
        return sourcePathRepository.save(toEntity(new SourcePath(), sourcePathDto));
    }

    @Override
    public SourcePath updateSourcePath(Long id, SourcePathDto sourcePathDto) {
        SourcePath updatedSourcePath = getById(id);
        return sourcePathRepository.save(toEntity(updatedSourcePath, sourcePathDto));
    }

    private SourcePath toEntity(SourcePath updatedSourcePath, SourcePathDto sourcePathDto) {
        updatedSourcePath.setTitle(validatedTitle(updatedSourcePath, sourcePathDto));
        updatedSourcePath.setPath(validatedPath(updatedSourcePath, sourcePathDto));
        updatedSourcePath.setLibraryItem(validatedType(updatedSourcePath, sourcePathDto));
        updatedSourcePath.setPathType(validatedPathLibraryItem(updatedSourcePath, sourcePathDto));

        EntityExistsException ex = existException(updatedSourcePath);
        if (ex != null) throw ex;

        return updatedSourcePath;
    }

    private EntityExistsException existException(SourcePath updatedSourcePath){
        for(SourcePath.PathType pathType : SourcePath.PathType.values()) {
            if(pathType != SourcePath.PathType.EXTERNAL) {
                for (LibraryItems libraryItem : LibraryItems.values()) {
                    Optional<SourcePath> movieDownloadSource = sourcePathRepository.findSourcePathByPathTypeAndMediaType(pathType, libraryItem);
                    if (movieDownloadSource.isPresent() && updatedSourcePath.getPathType().equals(pathType) && updatedSourcePath.getLibraryItem().equals(libraryItem)) {
                        return new EntityExistsException(String.format("Source path with type: %s and library item already: %s already exist", pathType, libraryItem));
                    }
                }
            }
        }return null;
    }

    private String validatedTitle(SourcePath sourcePath, SourcePathDto sourcePathDto){
        String titleDto = sourcePathDto.getTitle();
        String title = sourcePath.getTitle();
        if(titleDto == null){
            return title;
        }
        Optional<SourcePath> sourcePathWithTitle = sourcePathRepository.findByTitle(titleDto);
        if(sourcePathWithTitle.isPresent()){
            throw new EntityExistsException(String.format("Source path with title %s already exist",titleDto));
        }
        return titleDto;
    }

    private String validatedPath(SourcePath sourcePath, SourcePathDto sourcePathDto){
        String pathDto = sourcePathDto.getPath();
        String path = sourcePath.getPath();
        if(pathDto == null){
            return path;
        }
        Optional<SourcePath> sourcePathWithPath = sourcePathRepository.findByPath(pathDto);
        if(sourcePathWithPath.isPresent()){
            throw new EntityExistsException(String.format("Path %s already exist",pathDto));
        }
        return pathDto;
    }

    private LibraryItems validatedType(SourcePath sourcePath, SourcePathDto sourcePathDto){
        LibraryItems typeDto = sourcePathDto.getLibraryItem();
        LibraryItems type = sourcePath.getLibraryItem();
        if(typeDto == null){
            return type;
        }
        return typeDto;
    }

    private SourcePath.PathType validatedPathLibraryItem(SourcePath sourcePath, SourcePathDto sourcePathDto){
        SourcePath.PathType pathTypeDto = sourcePathDto.getPathType();
        SourcePath.PathType pathType = sourcePath.getPathType();

        if(pathTypeDto == null){
            return pathType;
        }
        return pathTypeDto;
    }
}
