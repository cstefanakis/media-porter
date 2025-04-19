package org.sda.mediaporter.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class AudioCodec {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
}
