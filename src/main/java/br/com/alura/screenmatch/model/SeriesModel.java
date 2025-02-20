package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeriesModel(@JsonAlias("Title") String title,
                          @JsonAlias("totalSeasons") Integer totalSeasons,
                          @JsonAlias("imdbRating") String rating,
                          @JsonAlias("Genre") String genre,
                          @JsonAlias("Plot") String plot,
                          @JsonAlias("Poster") String poster,
                          @JsonAlias("Actores") String actores
                          ) {
}