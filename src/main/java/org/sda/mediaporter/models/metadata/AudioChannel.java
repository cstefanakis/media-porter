package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Configuration;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "audio_channels")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AudioChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "channels")
    private Integer channels;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "audioChannel",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Audio> audios;
}
