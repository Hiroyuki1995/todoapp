package com.example.todoapp.util;

import java.util.Map;

import org.springframework.web.reactive.function.client.WebClient;

import com.example.todoapp.factory.WebClientFactory;

public class WebClientUtil {

  private static final WebClient webClient = WebClientFactory.createWebClient();

  public static String getRequest(String uri, Map<String, String> headers) {

    WebClient.RequestHeadersSpec<?> request = webClient.get()
        .uri(uri);

    headers.forEach(request::header); // ヘッダーを追加

    return request.retrieve()
        .bodyToMono(String.class)
        .block();
  }

  public static String postRequest(String uri, Object requestBody) {
    return webClient.post()
        .uri(uri)
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(String.class)
        .block();
  }

  // 他のHTTPメソッドについても同様にメソッドを追加できます
}
