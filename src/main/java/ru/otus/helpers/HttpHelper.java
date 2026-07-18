package ru.otus.helpers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpHelper {

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public HttpHelper() {
        client = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    public String get(String url) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {

            HttpResponse<String> response =
                    client.send(request,
                            HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (IOException | InterruptedException e) {

            throw new RuntimeException(e);

        }

    }

    public <T> T get(String url, Class<T> clazz) {

        try {

            return objectMapper.readValue(get(url), clazz);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public <T> List<T> getList(String url, Class<T> clazz) {

        try {

            JavaType type = objectMapper
                    .getTypeFactory()
                    .constructCollectionType(List.class, clazz);

            return objectMapper.readValue(get(url), type);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}