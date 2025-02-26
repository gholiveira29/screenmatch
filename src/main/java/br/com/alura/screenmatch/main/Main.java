package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.DeserializeData;
import br.com.alura.screenmatch.service.SearchApi;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final Scanner inputField = new Scanner(System.in);
    private final SearchApi searchApi = new SearchApi();
    private final DeserializeData deserializeData = new DeserializeData();
    private final String ULR = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=7e3bbab3";
    private final SerieRepository serieRepository;

    public Main(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void showMenu() {
        var option = -1;
        while (option != 0) {
            displayMenu();
            option = inputField.nextInt();
            inputField.nextLine();
            processMenuOption(option);
        }
    }

    private void displayMenu() {
        var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                4 - Buscar serie pelo nome
                5 - Top 5
                6 - Buscar por categoria
                
                0 - Sair
                """;
        System.out.println(menu);
    }

    private void processMenuOption(int option) {
        switch (option) {
            case 1:
                searchSerieWeb();
                break;
            case 2:
                searchEpisodeBySeries();
                break;
            case 3:
                listSeriesSearched();
                break;
            case 4:
                searchSerieByName();
                break;
            case 5:
                searchTopFiveSeries();
                break;
            case 6:
                searchByGenre();
                break;
            case 0:
                System.out.println("Saindo...");
                break;
            default:
                System.out.println("Opção inválida");
        }
    }


    private void searchSerieWeb() {
        SeriesModel response = getDadosSeries();
        Serie serie = new Serie(response);
        serieRepository.save(serie);
    }

    private SeriesModel getDadosSeries() {
        System.out.println("Digite o nome da série para busca");
        var serieName = inputField.nextLine();
        var json = searchApi.getData(ULR + serieName.replace(" ", "+") + API_KEY);
        return deserializeData.obterDados(json, SeriesModel.class);
    }

    private void searchEpisodeBySeries() {
        listSeriesSearched();
        System.out.println("Digite o nome da série para buscar episódios");
        var nameSerie = inputField.nextLine();
        Optional<Serie> serie = serieRepository.findByTitleContainingIgnoreCase(nameSerie);

        if (serie.isPresent()) {
            var seriesData = serie.get();
            List<SeasonModel> season = fetchSeasons(seriesData);
            season.forEach(System.out::println);
            List<Episode> episodes = season.stream()
                    .flatMap(d -> d.episode().stream()
                            .map(e -> new Episode(d.number(), e)))
                    .collect(Collectors.toList());
            seriesData.setEpisodes(episodes);
            serieRepository.save(seriesData);
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private List<SeasonModel> fetchSeasons(Serie seriesData) {
        List<SeasonModel> season = new ArrayList<>();
        for (int i = 1; i <= seriesData.getTotalSeasons(); i++) {
            var json = searchApi.getData(ULR + seriesData.getTitle().replace(" ", "+") + "&season=" + i + API_KEY);
            SeasonModel seasonModel = deserializeData.obterDados(json, SeasonModel.class);
            season.add(seasonModel);
        }
        return season;
    }

    private void listSeriesSearched() {
        List<Serie> series = serieRepository.findAll();
        series.stream().sorted(Comparator.comparing(Serie::getGenre)).forEach(System.out::println);
    }

    private void searchSerieByName() {
        System.out.println("Digite o nome da série para busca");
        var serieName = inputField.nextLine();

        Optional<Serie> serie = serieRepository.findByTitleContainingIgnoreCase(serieName);

        try {
            System.out.println(serie.get());
        } catch (NoSuchElementException e) {
            System.out.println("Série não encontrada");
        }
    }

    private void searchTopFiveSeries() {
        List<Serie> topFiveSeries = serieRepository.findTop5ByOrderByRatingDesc();
        topFiveSeries.forEach(
                serie -> System.out.println(serie.getTitle() + " - " + serie.getRating())
        );

    }
    private static final Map<String, String> genreTranslationMap = Map.of(
            "acao", "Action",
            "drama", "Drama",
            "comedia", "Comedy",
            "romance", "Romance",
            "crime", "Crime"
    );

    private void searchByGenre() {
        System.out.println("Digite o nome da categoria para busca");
        var genreInPortuguese = inputField.nextLine();
        var normalizedGenre = normalizeString(genreInPortuguese);
        var genre = genreTranslationMap.getOrDefault(normalizedGenre, normalizedGenre);
        Category genreEnum = Category.getCategory(genre);

        List<Serie> series = serieRepository.findByGenre(genreEnum);
        series.forEach(System.out::println);
    }

    private String normalizeString(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }
}