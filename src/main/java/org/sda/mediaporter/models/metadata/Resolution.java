package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Configuration;

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

    @Column(name = "names")
    private String name;

    @ManyToMany(mappedBy = "videoResolutions")
    @JsonBackReference("videoResolutions")
    private List<Configuration> resolutionsConfiguration;

}
