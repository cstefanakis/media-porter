package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.VideoFilePath;

@Entity
@Table(name = "subtitles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Subtitle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    @JsonManagedReference
    private Language language;

    @ManyToOne
    @JoinColumn(name = "codec_id")
    @JsonManagedReference
    private Codec codec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "video_file_path_id", nullable = false)
    @JsonBackReference
    private VideoFilePath videoFilePath;
}
