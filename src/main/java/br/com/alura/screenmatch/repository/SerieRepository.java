package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Category;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTitleContainingIgnoreCase(String title);

    List<Serie> findTop5ByOrderByRatingDesc();

    List<Serie> findByGenre(Category category);
}
