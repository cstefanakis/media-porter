package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.SourcePathService;
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
    public SourcePath createSourcePath(SourcePath sourcePath) {
        return sourcePathRepository.save(toEntity(new SourcePath(), sourcePath));
    }

    private SourcePath toEntity(SourcePath updatedSourcePath, SourcePath sourcePath) {
        updatedSourcePath.setTitle(sourcePath.getPath() == null ? updatedSourcePath.getTitle() : validatedTitle(sourcePath.getTitle()));
        updatedSourcePath.setPath(sourcePath.getPath() == null ? updatedSourcePath.getPath() : validatedPath(sourcePath.getPath()));
        updatedSourcePath.setType(sourcePath.getType() == null ? updatedSourcePath.getType() : sourcePath.getType());
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
    public SourcePath updateSourcePath(Long id, SourcePath sourcePath) {
        SourcePath updatedSourcePath = getById(id);
        return sourcePathRepository.save(toEntity(updatedSourcePath, sourcePath));
    }
}
