package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.CodecService;
import org.sda.mediaporter.Servicies.VideoService;
import org.sda.mediaporter.models.enums.Resolutions;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final CodecService codecService;

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository, CodecService codecService) {
        this.videoRepository = videoRepository;
        this.codecService = codecService;
    }

    @Override
    public Video createVideo(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public Video createVideoFromPath(Path file) {
        String[] properties = videoInfo(file).split(",");
        System.out.println(properties[4]);
        return createVideo(
                Video.builder()
                        .codec(codecService.autoCreateCodec(properties[1]))
                        .resolution(generatedResolution(properties[2], properties[3]))
                        .bitrate(FileServiceImpl.convertStringToInt(properties[4]))
                        .build()
        );
    }

    private String generatedResolution(String widthStr, String heightStr){
        Integer width = FileServiceImpl.convertStringToInt(widthStr);
        Integer height = FileServiceImpl.convertStringToInt(heightStr);
        for(Resolutions resolution : Resolutions.values()){
            if(width !=null &&
                    resolution.getWidth() == width && height != null &&
                    resolution.getHeight() == height ){
                return resolution.getLabel();
            }

            if(width != null && resolution.getWidth() == width){
                return resolution.getLabel();
            }
        }return null;
    }

    private String videoInfo(Path filePath){
        return  FileServiceImpl.runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=index,codec_name,width,height,bit_rate",
                "-of", "csv=p=0",
                filePath.toString()
        });
    }
}
