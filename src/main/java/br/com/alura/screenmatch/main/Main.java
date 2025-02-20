package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.SeasonModel;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.SearchApi;

import br.com.alura.screenmatch.model.SeriesModel;
import br.com.alura.screenmatch.service.DeserializeData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private final Scanner inputField = new Scanner(System.in);
    private final SearchApi searchApi = new SearchApi();
    private final DeserializeData deserializeData = new DeserializeData();
    private final String ULR = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=7e3bbab3";
    private final List<SeriesModel> seriesList = new ArrayList<>();
    private final SerieRepository serieRepository;

    public Main(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }


    public void showMenu() {
        var option = -1;
        while (option != 0) {

            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    
                    0 - Sair
                    """;

            System.out.println(menu);

            option = inputField.nextInt();
            inputField.nextLine();
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
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
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
        SeriesModel dados = deserializeData.obterDados(json, SeriesModel.class);
        return dados;
    }

    private void searchEpisodeBySeries() {
        SeriesModel seriesData = getDadosSeries();
        List<SeasonModel> season = new ArrayList<>();

        for (int i = 1; i <= seriesData.totalSeasons(); i++) {
            var json = searchApi.getData(ULR + seriesData.title().replace(" ", "+") + "&season=" + i + API_KEY);
            SeasonModel seasonModel = deserializeData.obterDados(json, SeasonModel.class);
            season.add(seasonModel);
        }
        season.forEach(System.out::println);
    }

    private void listSeriesSearched() {
        List<Serie> series = serieRepository.findAll();
        series.stream().sorted(Comparator.comparing(Serie::getGenre)).forEach(System.out::println);
    }
}