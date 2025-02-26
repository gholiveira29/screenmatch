package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double rating;
    private LocalDate releaseData;

    @ManyToOne
    private Serie serie;

    public Episode() {
    }

    public Episode(Integer seasonNumber, EpisodeModel episodeModel) {
        this.season = seasonNumber;
        this.title = episodeModel.title();
        this.episodeNumber = episodeModel.number();

        try {
            this.rating = Double.valueOf(episodeModel.rating());
        } catch (NumberFormatException ex) {
            this.rating = 0.0;
        }

        try {
            this.releaseData = LocalDate.parse(episodeModel.releaseData());
        } catch (DateTimeParseException ex) {
            this.releaseData = null;
        }
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getReleaseData() {
        return releaseData;
    }

    public void setReleaseData(LocalDate releaseData) {
        this.releaseData = releaseData;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "temporada=" + season +
                ", title='" + title + '\'' +
                ", numeroEpisodio=" + episodeNumber +
                ", rating=" + rating +
                ", releaseData=" + releaseData;
    }
}
