package br.com.alura.screenmatch.service;

public interface IDeserializeData {
    <T> T  obterDados(String json, Class<T> classe);
}
