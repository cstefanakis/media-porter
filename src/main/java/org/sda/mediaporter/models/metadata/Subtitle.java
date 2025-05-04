package org.sda.mediaporter.models.metadata;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.models.Language;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subtitle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Language language;
    @ManyToOne
    private Codec format;
}
