package org.sda.mediaporter.services.videoServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.repositories.metadata.ResolutionRepository;
import org.sda.mediaporter.services.videoServices.impl.ResolutionServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResolutionServiceTest {

    @Mock
    ResolutionRepository resolutionRepository;

    @InjectMocks
    ResolutionServiceImpl resolutionService;

    private Resolution fullHd;
    private Resolution hd;

    @BeforeEach
    void setup(){
        fullHd = Resolution.builder()
                .name("FullHD")
                .build();
        hd = Resolution.builder()
                .name("HD")
                .build();
    }

    @Test
    void getResolutionByName() {
        //Arrest
        String fullHd = "FullHD";
        when(resolutionRepository.findByName(fullHd))
                .thenReturn(Optional.of(this.fullHd));

        //Act
        Resolution result = resolutionService.getResolutionByName(fullHd);

        //Assert
        assertNotNull(result);
        assertEquals(fullHd, result.getName());
        verify(resolutionRepository).findByName(fullHd);
    }

    @Test
    void getAllResolutions() {
        //Arrest
        List<Resolution> resolutions = List.of(this.fullHd, this.hd);
        when(resolutionRepository.findAll())
                .thenReturn(resolutions);
        //Act
        List<Resolution> result = resolutionService.getAllResolutions();

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(resolutionRepository).findAll();
    }

    @Test
    void getResolutionById() {
        //Arrest
        Long id = 1L;
        when(resolutionRepository.findById(id))
                .thenReturn(Optional.of(this.fullHd));
        //Act
        Resolution result = resolutionService.getResolutionById(id);

        //Assert
        assertNotNull(result);
        assertEquals("FullHD", result.getName());
        verify(resolutionRepository).findById(id);
    }
}