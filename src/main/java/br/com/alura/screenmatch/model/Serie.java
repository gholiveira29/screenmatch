package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.translater.SearchMyMemory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String title;

    @Enumerated(EnumType.STRING)
    private Category genre;

    @Transient
    private List<Episode> episodes = new ArrayList<>();

    private Integer totalSeasons;
    private Double rating;
    private String plot;
    private String poster;
    private String actores;


public Serie() {
    }

    public Serie(SeriesModel seriesModel) {
        this.title = seriesModel.title();
        this.totalSeasons = seriesModel.totalSeasons();
        this.rating = OptionalDouble.of(Double.parseDouble(seriesModel.rating())).orElse(0.0);
        this.genre = Category.getCategory(seriesModel.genre().split(",")[0].trim());
        this.plot = SearchMyMemory.getTranslation(seriesModel.plot().trim());
        this.poster = seriesModel.poster();
        this.actores = seriesModel.actores();
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Serie{" +
                "title='" + title + '\'' +
                ", totalSeasons=" + totalSeasons +
                ", rating=" + rating +
                ", genre=" + genre +
                ", plot='" + plot + '\'' +
                ", poster='" + poster + '\'' +
                ", actores='" + actores + '\'' +
                '}';
    }
}
