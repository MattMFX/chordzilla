package br.edu.ufabc.mfmachado.chordzilla.api.model;

public record Response<T>(StatusCode statusCode, T body) {

    public enum StatusCode {
        OK,
        NOT_FOUND,
        INTERNAL_SERVER_ERROR
    }
}
