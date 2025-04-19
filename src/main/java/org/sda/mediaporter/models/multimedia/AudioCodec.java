package org.sda.mediaporter.models.multimedia;

import jakarta.persistence.*;

@Entity
public class AudioCodec {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
}
