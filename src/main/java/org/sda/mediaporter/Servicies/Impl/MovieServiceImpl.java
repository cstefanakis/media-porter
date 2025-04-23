package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.MovieService;
import org.sda.mediaporter.api.OmdbApi;
import org.sda.mediaporter.api.TheMovieDb;
import org.sda.mediaporter.models.Movie;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Override
    public Movie getMovieByTitle(String title, String year) {
        return toEntity(new Movie(), getOriginalTitle(title), year);
    }

    private Movie toEntity(Movie movie, String title, String year){
        TheMovieDb theMovieDb = new TheMovieDb(title);
        OmdbApi omdbApi = new OmdbApi(title, year);
        movie.setTitle(getTitle(omdbApi, theMovieDb));
        movie.setYear(getYear(omdbApi, theMovieDb));
        movie.setRating(getRate(omdbApi, theMovieDb));
        movie.setReleaseDate(omdbApi.getReleasedDate());
        movie.setGenre(Arrays.asList(omdbApi.getGenre()));
        movie.setDirectors(Arrays.asList(omdbApi.getDirector()));
        movie.setWriters(Arrays.asList(omdbApi.getWriter()));
        movie.setActors(Arrays.asList(omdbApi.getActors()));
        movie.setPlot(omdbApi.getPlot());
        movie.setCountry(omdbApi.getCountry());
        movie.setPoster(omdbApi.getPoster());
        return movie;
    }

    private String getOriginalTitle(String title){
        TheMovieDb theMovieDb = new TheMovieDb(title);
        return theMovieDb.getOriginalTitle();
    }

    private String getTitle(OmdbApi omdbApi, TheMovieDb theMovieDb){
        if(omdbApi.getTitle() == null){
            return theMovieDb.getOriginalTitle();
        }else if(theMovieDb.getOriginalTitle() == null){
            return null;
        }else{
            return omdbApi.getTitle();
        }
    }

    private Integer getYear(OmdbApi omdbApi, TheMovieDb theMovieDb){
        if(omdbApi.getYear() == null && theMovieDb.getReleaseDate() == null){
            return null;
        }
        if (omdbApi.getYear() == null){
            return theMovieDb.getReleaseDate().getYear();
        }
        return omdbApi.getYear();
    }

    private Double getRate(OmdbApi omdbApi, TheMovieDb theMovieDb){
        if(omdbApi.getImdbRating() == null){
            return null;
        }else{
            return omdbApi.getImdbRating();
        }
    }

    private LocalDate getReleasedDate(OmdbApi omdbApi, TheMovieDb theMovieDb){
        if(omdbApi.getReleasedDate() == null && theMovieDb.getReleaseDate() == null){
            return null;
        }
        if(omdbApi.getReleasedDate() == null){
            return theMovieDb.getReleaseDate();
        }
        return omdbApi.getReleasedDate();
    }

    private List<String> getGenre(OmdbApi omdbApi){
        if(omdbApi.getGenre() == null){
            return null;
        }
        return Arrays.asList(omdbApi.getGenre());
    }

    private List<String> getDirectors(OmdbApi omdbApi){
        if(omdbApi.getDirector() == null){
            return null;
        }
        return Arrays.asList(omdbApi.getDirector());
    }

    private List<String> getWriters(OmdbApi omdbApi){
        if(omdbApi.getWriter() == null){
            return null;
        }
        return Arrays.asList(omdbApi.getWriter());
    }

    private List<String> getActors(OmdbApi omdbApi){
        if(omdbApi.getActors() == null){
            return null;
        }
        return Arrays.asList(omdbApi.getActors());
    }

    private String getPlot(OmdbApi omdbApi, TheMovieDb theMovieDb){
        if(omdbApi.getPlot() == null && theMovieDb.getOverview() == null){
            return null;
        }
        if(omdbApi.getPlot() == null){
            return theMovieDb.getOverview();
        }
        return omdbApi.getPlot();
    }

    private String getCountry(OmdbApi omdbApi){
        if(omdbApi.getCountry() == null){
            return null;
        }
        return omdbApi.getCountry();
    }

    private String getPoster(OmdbApi omdbApi, TheMovieDb theMovieDb){
        if(omdbApi.getPoster() == null && theMovieDb.getOverview() == null){
            return null;
        }
        if (omdbApi.getPoster() == null){
            return theMovieDb.getPoster();
        }
        return omdbApi.getPoster();
    }
}
