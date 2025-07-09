package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.enums.MediaTypes;

import java.util.ArrayList;
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
    @NotEmpty
    private String name;
    @Enumerated(EnumType.STRING)
    @NotNull
    private MediaTypes mediaType;
}
