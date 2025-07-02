package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.SourcePathService;
import org.sda.mediaporter.dtos.SourcePathDto;
import org.sda.mediaporter.models.SourcePath;
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

    private SourcePath toEntity(SourcePath updatedSourcePath, SourcePathDto sourcePathDto) {
        updatedSourcePath.setTitle(sourcePathDto.getPath() == null ? updatedSourcePath.getTitle() : validatedTitle(sourcePathDto.getTitle()));
        updatedSourcePath.setPath(sourcePathDto.getPath() == null ? updatedSourcePath.getPath() : validatedPath(sourcePathDto.getPath()));
        updatedSourcePath.setType(sourcePathDto.getType() == null ? updatedSourcePath.getType() : sourcePathDto.getType());
        updatedSourcePath.setPathType(sourcePathDto.getPathType() == null ? updatedSourcePath.getPathType() : sourcePathDto.getPathType());
        return updatedSourcePath;
    }

    private String validatedPath(String path) {
        Optional <SourcePath> optional = sourcePathRepository.findByPath(path);
        if (optional.isPresent()) {
            throw new EntityExistsException(String.format("Source path with path %s already exists", path));
        }return path;
    }

    private String validatedTitle(String title) {
        Optional <SourcePath> optional = sourcePathRepository.findByTitle(title);
        if (optional.isPresent()) {
            throw new EntityExistsException(String.format("Source path with title %s already exists", title));
        }return title;
    }

    @Override
    public SourcePath updateSourcePath(Long id, SourcePathDto sourcePathDto) {
        SourcePath updatedSourcePath = getById(id);
        return sourcePathRepository.save(toEntity(updatedSourcePath, sourcePathDto));
    }
}
