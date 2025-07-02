package org.sda.mediaporter.models.metadata;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name = "resolutions")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resolution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    private String name;
}
