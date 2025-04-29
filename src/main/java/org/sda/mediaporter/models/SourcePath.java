package org.sda.mediaporter.models;

import jakarta.persistence.*;

@Entity
public class SourcePath {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String path;
    @Column(columnDefinition = "TEXT")
    String description;

}
