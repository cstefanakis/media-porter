package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "resolutions")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resolution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // AUTO uses provider defaults (can be sequence or identity)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy ="resolution")
    @JsonBackReference
    private List<Video> videos;
}
