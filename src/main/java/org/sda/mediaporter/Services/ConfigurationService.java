package org.sda.mediaporter.Services;

import org.sda.mediaporter.dtos.ConfigurationDto;
import org.sda.mediaporter.models.Configuration;

public interface ConfigurationService {
    Configuration getConfiguration();
    void updateConfiguration(ConfigurationDto configurationDto);
}
