package org.sda.mediaporter.models.multimedia.videoFiles;

import jakarta.persistence.*;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.models.ContributorRole;

import java.util.List;

@Entity
public class MovieContributor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "contributor_id")
    private Contributor contributor;
    @ManyToMany
    @JoinTable(
            name = "movie_contributors",
            joinColumns = @JoinColumn(name ="contributor_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_role_id")

    )
    private List<ContributorRole> contributorRoles;
}
