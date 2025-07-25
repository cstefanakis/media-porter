package org.sda.mediaporter.controllers;

import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sda.mediaporter.Services.ContributorService;
import org.sda.mediaporter.models.Contributor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contributors")
public class ContributorController {

    private final ContributorService contributorService;

    public ContributorController(ContributorService contributorService) {
        this.contributorService = contributorService;
    }

    @GetMapping()
    public ResponseEntity<Page<Contributor>> getAllContributors(Pageable pageable){
        Page<Contributor> contributors = contributorService.getAllContributors(pageable);
        return ResponseEntity.ok(contributors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contributor> getContributorById(@PathVariable("id") Long id){
        Contributor contributor = contributorService.getContributorById(id);
        return ResponseEntity.ok(contributor);
    }

    @GetMapping("/by-full-name")
    public ResponseEntity<Contributor> getContributorByFullName(@RequestParam("fullName") String fullName){
        Contributor contributor = contributorService.getContributorByFullName(fullName);
        return ResponseEntity.ok(contributor);
    }
}
