package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.ResolutionService;
import org.sda.mediaporter.dtos.ResolutionDto;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.repositories.metadata.ResolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResolutionServiceImpl implements ResolutionService {

    private final ResolutionRepository resolutionRepository;

    @Autowired
    public ResolutionServiceImpl(ResolutionRepository resolutionRepository) {
        this.resolutionRepository = resolutionRepository;
    }

    @Override
    public Resolution getResolutionByName(String name) {
        return resolutionRepository.findByName(name).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Resolution with name %s not found",name)));
    }

    @Override
    public List<Resolution> getAllResolutions() {
        return resolutionRepository.findAll();
    }

    @Override
    public Resolution createResolution(ResolutionDto resolutionDto) {
        return resolutionRepository.save(toEntity(new Resolution(), resolutionDto));
    }

    @Override
    public void updateResolution(Long resolutionId, ResolutionDto resolutionDto) {
        Resolution resolution = getResolutionById(resolutionId);
        resolutionRepository.save(toEntity(resolution, resolutionDto));
    }

    private Resolution toEntity(Resolution resolution, ResolutionDto resolutionDto){
        resolution.setName(validatedResolutionName(resolution, resolutionDto));
        return resolution;
    }

    private String validatedResolutionName(Resolution resolution, ResolutionDto resolutionDto){
        String resolutionName = resolution.getName();
        String resolutionNameDto = resolutionDto.getName();

        if(resolutionName != null && resolutionNameDto == null){
            return resolutionName;
        }
        Optional <Resolution> resolutionOptional = resolutionRepository.findByName(resolutionNameDto);
        if(resolutionOptional.isEmpty()){
            return resolutionNameDto.toLowerCase();
        }
        throw new EntityExistsException(String.format("Resolution name with name %s already exist", resolutionNameDto));
    }

    @Override
    public Resolution getResolutionById(Long id) {
        return resolutionRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Resolution with id %s not exist", id))
        );
    }

    @Override
    public void deleteResolution(Long id) {
        Resolution resolution = resolutionRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Resolution with id %s not exist", id))
        );
        resolutionRepository.delete(resolution);
    }

    private String validatedResolution(Resolution resolution, String resolutionName){
        String name = resolution.getName();
        if(resolutionName == null && name != null){
            return name;
        }
        Optional<Resolution> resolutionOptional = resolutionRepository.findByName(resolutionName);
        if(resolutionOptional.isEmpty()){
            return resolutionName;
        }
        throw new EntityExistsException(String.format("Resolution with name %s already exist", resolutionName));
    }
}
