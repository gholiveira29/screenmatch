package br.com.alura.screenmatch.service.translater;

import br.com.alura.screenmatch.service.SearchApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;

public class SearchMyMemory {
    public static String getTranslation(String text) {
        ObjectMapper mapper = new ObjectMapper();

        SearchApi searchApi = new SearchApi();

        String translationText = URLEncoder.encode(text);
        String langpair = URLEncoder.encode("en|pt-br");

        String url = "https://api.mymemory.translated.net/get?q=" + translationText + "&langpair=" + langpair;

        String json = searchApi.getData(url);

        TranslationData translation;
        try {
            translation = mapper.readValue(json, TranslationData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return translation.responseData().translatedText();
    }
}
