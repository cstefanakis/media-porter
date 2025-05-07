package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.VideoService;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public Video createVideo(Video video) {
        return videoRepository.save(video);
    }
}
