package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonModel(@JsonAlias("Season") Integer number,
                          @JsonAlias("Episodes") List<EpisodeModel> episode) {
}
