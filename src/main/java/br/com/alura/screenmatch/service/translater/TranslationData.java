package br.com.alura.screenmatch.service.translater;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TranslationData(@JsonAlias(value = "responseData") ResponseData responseData) {
}
