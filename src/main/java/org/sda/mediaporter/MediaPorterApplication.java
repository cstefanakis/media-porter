package org.sda.mediaporter;

import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.movieServices.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class MediaPorterApplication {

    @Autowired
    private MovieService movieService;

    @Autowired
    private FileService fileService;

    public static void main(String[] args) {
        SpringApplication.run(MediaPorterApplication.class, args);
    }

}
