package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.ResolutionService;
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
    public Resolution createResolution(String resolutionName) {
        Optional<Resolution> resolutionOptional = resolutionRepository.findByName(resolutionName);
        if(resolutionOptional.isEmpty()){
            return resolutionRepository.save(Resolution.builder()
                            .name(resolutionName)
                    .build());
        }
        throw new EntityExistsException(String.format("Resolution with name %s already exist", resolutionName));
    }

    @Override
    public void updateResolution(Long resolutionId, String resolutionName) {
        Optional <Resolution> resolutionOptional = resolutionRepository.findById(resolutionId);
        resolutionOptional.ifPresent(resolution -> resolution.setName(resolutionName));
        throw new EntityExistsException(String.format("Resolution with id %s not exist", resolutionId));
    }

    @Override
    public void deleteResolution(Long id) {
        Resolution resolution = resolutionRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Resolution with id %s not exist", id))
        );
        resolutionRepository.delete(resolution);
    }
}
