package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.enums.MediaTypes;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "codecs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Codec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private MediaTypes mediaType;

    @OneToMany(mappedBy = "codec")
    private List<Audio> audios;

    @OneToMany(mappedBy = "codec")
    private List<Video> videos;

    @OneToMany(mappedBy = "codec")
    private List<Subtitle> subtitles;

}
