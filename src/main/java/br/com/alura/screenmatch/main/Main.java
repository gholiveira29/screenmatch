package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.EpisodeModel;
import br.com.alura.screenmatch.model.SeasonModel;
import br.com.alura.screenmatch.model.SeriesModel;
import br.com.alura.screenmatch.service.DeserializeData;
import br.com.alura.screenmatch.service.SearchApi;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner inputField = new Scanner(System.in);
    private SearchApi searchData = new SearchApi();
    private DeserializeData deserializeData = new DeserializeData();

    private final String URL = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void showMenu(){
        System.out.println("Digite o nome da série para busca");
        var serieName = inputField.nextLine();
        var json = searchData.obterDados(URL + serieName.replace(" ", "+") + API_KEY);
        SeriesModel response = deserializeData.obterDados(json, SeriesModel.class);
        System.out.println(response);

        List<SeasonModel> seasonModelList = new ArrayList<>();

        for (int i = 1; i<= response.totalSeasons(); i++){
            json = searchData.obterDados(URL + serieName.replace(" ", "+") +"&season=" + i + API_KEY);
            SeasonModel seasonModel = deserializeData.obterDados(json, SeasonModel.class);
            seasonModelList.add(seasonModel);
        }
        seasonModelList.forEach(System.out::println);

//        for(int i = 0; i < response.totalSeasons(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episode();
//            for(int j = 0; j< episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).title());
//            }
//        }

        seasonModelList.forEach(t -> t.episode().forEach(e -> System.out.println(e.title())));

//        List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");
//
//        nomes.stream().sorted().limit(3).filter(n -> n.startsWith("N")).map(n -> n.toUpperCase())
//                .forEach(System.out::println);

        List<EpisodeModel> episodeModels = seasonModelList.stream()
                .flatMap(t -> t.episode().stream())
                .collect(Collectors.toList());

//        System.out.println("\nTop 10 episódios");
//        dadosEpisodios.stream()
//                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro(N/A) " +  e))
//                .sorted(Comparator.comparing(DadosEpisodio::rating).reversed())
//                .peek(e -> System.out.println("Ordenação " +  e))
//                .limit(10)
//                .peek(e -> System.out.println("Limite " +  e))
//                .map(e -> e.title().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento " +  e))
//                .forEach(System.out::println);

        List<Episode> episodes = seasonModelList.stream()
                .flatMap(t -> t.episode().stream()
                        .map(d -> new Episode(t.number(), d)))
                .collect(Collectors.toList());

        episodes.forEach(System.out::println);

//        System.out.println("Digite um trecho do título do episódio");
//        var trechoTitulo = leitura.nextLine();
//        Optional<Episodio> episodioBuscado = episode.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//        if(episodioBuscado.isPresent()){
//            System.out.println("Episódio encontrado!");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//        } else {
//            System.out.println("Episódio não encontrado!");
//        }
//
//        System.out.println("A partir de que ano você deseja ver os episódios? ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episode.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada:  " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                " Data lançamento: " + e.getDataLancamento().format(formatador)
//                ));

        Map<Integer, Double> avaliacoesPorTemporada = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason, Collectors.averagingDouble(Episode::getRating)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());
    }
}