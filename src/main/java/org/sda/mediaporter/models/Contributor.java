package org.sda.mediaporter.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contributors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contributor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String fullName;
    private String poster;
    private String website;
}
