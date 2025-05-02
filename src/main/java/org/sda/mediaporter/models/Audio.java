package org.sda.mediaporter.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer bitrate;
    private Integer channels;
    @ManyToOne(fetch = FetchType.LAZY)
    private Codec codec;
    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
