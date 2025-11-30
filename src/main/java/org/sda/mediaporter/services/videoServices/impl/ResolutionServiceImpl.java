package org.sda.mediaporter.services.videoServices.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.dtos.ResolutionDto;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.repositories.metadata.ResolutionRepository;
import org.sda.mediaporter.services.videoServices.ResolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
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
    public Resolution getResolutionById(Long id) {
        return resolutionRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Resolution with id %s not exist", id))
        );
    }
}
