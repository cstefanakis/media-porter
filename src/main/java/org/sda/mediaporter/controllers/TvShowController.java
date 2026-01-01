package org.sda.mediaporter.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sda.mediaporter.models.TvShow;
import org.sda.mediaporter.services.tvShowServices.TvShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tv-show")
public class TvShowController {

    private final TvShowService tvShowService;

    @Autowired
    public TvShowController(TvShowService tvShowService) {
        this.tvShowService = tvShowService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping()
    public ResponseEntity<Page<TvShow>> getAllRvShows(Pageable pageable) {
        Page<TvShow> tvShows = tvShowService.getTvShows(pageable);
        return ResponseEntity.ok(tvShows);
    }
}
