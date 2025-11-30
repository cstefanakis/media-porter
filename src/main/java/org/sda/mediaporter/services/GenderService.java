package org.sda.mediaporter.services;

import org.sda.mediaporter.models.Gender;

public interface GenderService {
    Gender getGenderOrNullByTitle(String genderTitle);
}
