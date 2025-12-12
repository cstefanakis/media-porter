package org.sda.mediaporter.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCastDto;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCrewDto;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TheMovieDbTvShowEpisodesTest {

    private TheMovieDbTvShowEpisodes dexter;

    @BeforeEach
    void setup(){
        this.dexter = new TheMovieDbTvShowEpisodes(259909L, 1, 1);
    }

    @Test
    void getAirDate() {
        //Arrest
        LocalDate airDate = LocalDate.of(2025, 7, 13);

        //Act
        LocalDate result = dexter.getTheMovieDbTvShowEpisodeDto().getAirDate();

        //Assert
        assertEquals(airDate, result);

    }

    @Test
    void getWriters() {
        //Arrest
        String writer = "Clyde Phillips";
        String poster = "https://image.tmdb.org/t/p/w500"+"/ue6gnd9FRbl5wKZ9yHVtWp6ra4y.jpg";

        //Act
        List<TheMovieDbCrewDto> result = dexter.getTheMovieDbTvShowEpisodeDto().getWriters();
        //Assert
        assertFalse(result.stream()
                .filter(n -> n.getFullName().equals(writer))
                .toList()
                .isEmpty());

        assertFalse(result.stream()
                .filter(n -> n.getPoster().equals(poster))
                .toList()
                .isEmpty());

        assertEquals(1, result.size());
    }

    @Test
    void getDirectors() {
        //Arrest
        String director = "Marcos Siega";
        String poster = "https://image.tmdb.org/t/p/w500"+"/uZD2ihL4iKzQxMtcLDH47AikT1V.jpg";

        //Act
        List<TheMovieDbCrewDto> result = dexter.getTheMovieDbTvShowEpisodeDto().getDirectors();
        //Assert
        assertFalse(result.stream()
                .filter(n -> n.getFullName().equals(director))
                .toList()
                .isEmpty());

        assertFalse(result.stream()
                .filter(n -> n.getPoster().equals(poster))
                .toList()
                .isEmpty());

        assertEquals(1, result.size());
    }

    @Test
    void getActors() {
        //Arrest
        String actor = "John Lithgow";
        String poster = "https://image.tmdb.org/t/p/w500"+"/8Y1sjBdnVR483S8PrnAQzlESwhx.jpg";

        //Act
        List<TheMovieDbCastDto> result = dexter.getTheMovieDbTvShowEpisodeDto().getActors();
        //Assert
        assertFalse(result.stream()
                .filter(n -> n.getFullName().equals(actor))
                .toList()
                .isEmpty());

        assertFalse(result.stream()
                .filter(n -> n.getPoster().equals(poster))
                .toList()
                .isEmpty());
    }

    @Test
    void getEpisodeNumber() {
        //Arrest
        Integer episodeNumber = 1;

        //Act
        Integer result = dexter.getTheMovieDbTvShowEpisodeDto().getEpisodeNumber();

        //Assert
        assertEquals(episodeNumber, result);
    }

    @Test
    void getEpisodeType() {
        //Arrest
        String episodeType = "standard";

        //Act
        String result = dexter.getTheMovieDbTvShowEpisodeDto().getEpisodeType();

        //Assert
        assertEquals(episodeType, result);
    }

    @Test
    void getSeasonNumber() {
        //Arrest
        Integer seasonNumber = 1;

        //Act
        Integer result = dexter.getTheMovieDbTvShowEpisodeDto().getSeasonNumber();

        //Assert
        assertEquals(seasonNumber, result);
    }

    @Test
    void getEpisodeName() {
        //Arrest
        String episodeName = "A Beating Heart...";

        //Act
        String result = dexter.getTheMovieDbTvShowEpisodeDto().getEpisodeName();

        //Assert
        assertEquals(episodeName, result);
    }

    @Test
    void getOverview() {
        //Arrest
        String overview = "Dexter Morgan awakens from a ten-week coma.";

        //Act
        String result = dexter.getTheMovieDbTvShowEpisodeDto().getOverview();

        //Assert
        assertEquals(overview, result);
    }

    @Test
    void getRate() {

        //Act
        Double result = dexter.getTheMovieDbTvShowEpisodeDto().getRate();

        //Assert
        assertTrue(result > 0);
    }

    @Test
    void getImage() {
        //Arrest
        String image = "https://image.tmdb.org/t/p/w500"+"/cdxZjP0S2K1FGO3NiXbQAM6TqwS.jpg";

        //Act
        String result = dexter.getTheMovieDbTvShowEpisodeDto().getPoster();

        //Assert
        assertEquals(image, result);
    }
}