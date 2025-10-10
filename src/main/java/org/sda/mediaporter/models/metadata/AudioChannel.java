package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Configuration;

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

    @Column(name = "titles")
    private String title;

    @Column(name = "channels")
    private Integer channels;

    @Column(name = "descriptions")
    private String description;

    @ManyToMany(mappedBy = "audioChannels")
    @JsonBackReference("audioChannels")
    private List<Configuration> audioChannelsConfiguration;
}
