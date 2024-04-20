package com.example.todoapp.factory;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

public class WebClientFactory {

    private static final String BASE_URL = "http://jsonplaceholder.typicode.com";

    public static WebClient createWebClient() {
        Builder webClientBuilder = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Content-Type", "application/json");

        return webClientBuilder.build();
    }
}
