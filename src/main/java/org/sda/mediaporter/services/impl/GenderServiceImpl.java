package org.sda.mediaporter.services.impl;

import org.sda.mediaporter.models.Gender;
import org.sda.mediaporter.repositories.GenderRepository;
import org.sda.mediaporter.services.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenderServiceImpl implements GenderService {

    private final GenderRepository genderRepository;

    @Autowired
    public GenderServiceImpl(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    @Override
    public Gender getGenderOrNullByTitle(String genderTitle) {
        Optional<Gender> genderOptional = genderRepository.findGenderByTitle(genderTitle);
        return genderOptional.orElse(null);
    }
}
