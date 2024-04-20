package com.example.todoapp.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.todoapp.exception.CustomException;
import com.example.todoapp.model.GoogleAccessTokenRequest;
import com.example.todoapp.util.WebClientUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GoogleService {

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String clientId;
  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String clientSecret;

  public String fetchAccessToken(String code) throws CustomException {
    try {
      String decodedUrl = URLDecoder.decode(code, StandardCharsets.UTF_8.toString());
      GoogleAccessTokenRequest googleAccessTokenRequest = new GoogleAccessTokenRequest(
          decodedUrl,
          clientId,
          clientSecret,
          "http://localhost:8081/login/oauth2/code/google",
          "authorization_code");
      // ObjectMapper インスタンスを作成
      ObjectMapper mapper = new ObjectMapper();

      try {
        // オブジェクトを JSON 文字列に変換
        String jsonString = mapper.writeValueAsString(googleAccessTokenRequest);
        System.out.println(jsonString);
      } catch (Exception e) {
        e.printStackTrace();

      }

      String jsonResponse = WebClientUtil.postRequest("https://accounts.google.com/o/oauth2/token",
          googleAccessTokenRequest);
      if (jsonResponse != null && !jsonResponse.isEmpty()) {
        // JSON 文字列から Map に変換して `access_token` を取得
        Map<String, Object> responseMap = mapper.readValue(jsonResponse, Map.class);
        return (String) responseMap.get("access_token"); // `access_token` を返却
      }
      return "";

    } catch (Exception e) {
      e.printStackTrace();
      throw new CustomException("error", e);
    }
  }

  public String fetchGoogleUserInfo(String accessToken) throws CustomException {
    try {
      Map<String, String> headers = new HashMap<>();
      ObjectMapper mapper = new ObjectMapper();
      headers.put("Authorization", "Bearer " + accessToken);
      String jsonResponse = WebClientUtil.getRequest("https://www.googleapis.com/oauth2/v1/userinfo", headers);
      if (jsonResponse != null && !jsonResponse.isEmpty()) {
        Map<String, Object> responseMap = mapper.readValue(jsonResponse, Map.class);
        return (String) responseMap.get("name"); // `access_token` を返却
      }
      return "";
    } catch (Exception e) {
      e.printStackTrace();
      throw new CustomException("error", e);
    }

  }

}
