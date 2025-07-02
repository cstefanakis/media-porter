package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.sda.mediaporter.Services.DownloaderService;
import org.sda.mediaporter.Services.FileService;
import org.sda.mediaporter.dtos.DownloadFileDto;
import org.sda.mediaporter.models.DownloadFile;
import org.sda.mediaporter.repositories.DownloaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class DownloaderServiceImpl implements DownloaderService {

    private final DownloaderRepository downloaderRepository;
    private final FileService fileService;

    @Autowired
    public DownloaderServiceImpl(DownloaderRepository downloaderRepository, FileService fileService) {
        this.downloaderRepository = downloaderRepository;
        this.fileService = fileService;
    }

    @Override
    public List<DownloadFile> downloads() {
        return downloaderRepository.findAll();
    }

    @Override
    public void deleteDownloadFileById(Long id) {
        DownloadFile downloadFile = downloaderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("File with id: %s not found",id)));
        downloaderRepository.delete(downloadFile);
    }

    @Override
    public DownloadFile createdDownloadFile(DownloadFileDto downloadFileDto) {
        return downloaderRepository.save(toEntity(downloadFileDto, new DownloadFile()));
    }

    private DownloadFile toEntity(DownloadFileDto downloadFileDto, DownloadFile downloadFile){
        downloadFile.setName(downloadFileDto.getName());
        downloadFile.setUrl(downloadFileDto.getUrl());
        downloadFile.setStatus(downloadFileDto.getStatus());
        downloadFile.setLibraryItems(downloadFileDto.getLibraryItem());
        return downloadFile;
    }


    @Override
    @Async
//    @Scheduled(fixedDelay = 3 * 60 * 1000)
    public void downloadFile() {
        List<DownloadFile> listFiles = downloaderRepository.findAll();
        int num = 0;
        if(!listFiles.isEmpty()){
            for(DownloadFile file : listFiles) {
                num ++;
                file.setStatus(DownloadFile.Status.IN_PROGRESS);
                Document htmlDocument = getHtmlDocument(file.getUrl());
                System.out.println(htmlDocument);
                String getTitle = getListHtmlElements(htmlDocument, "div.title_bar h1").getFirst().attr("title");
                file.setName(getTitle);
                Element elementDownload = getListHtmlElements(htmlDocument, "div.low-speed").getFirst();
                String downloadUrl = "https://fastshare.cloud/" + elementDownload.selectFirst("form#form").attr("action");
                String path = "Z:\\Downloads\\Movies\\" + "file.chunk" + num;
                downloaderRepository.save(file);
                downloadedFile(downloadUrl, path);
                Path pathPath = Path.of(path);

                file.setStatus(DownloadFile.Status.DOWNLOADED);
                downloaderRepository.delete(file);
                try {
                    Files.move(pathPath, pathPath.resolveSibling(getTitle));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void downloadedFile(String url, String savePath) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (InputStream in = conn.getInputStream();
                 FileOutputStream out = new FileOutputStream(savePath)) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                int totalRead = 0;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                out.flush();

                System.out.println("Download complete: " + savePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private Document getHtmlDocument(String url){
        try {
            return
                    Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Element> getListHtmlElements(Document htmlDocument, String element){
        List<Element> selectedElements = htmlDocument.select(element);
        if(!selectedElements.isEmpty()){
            return selectedElements;
        }else{
            return null;
        }
    }
}
