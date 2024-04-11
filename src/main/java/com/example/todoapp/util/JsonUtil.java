package com.example.todoapp.util;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

  public static String mapToJson(Map<String, Object> map) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      // MapをJSON文字列に変換
      return mapper.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Map<String, Object> jsonToMap(String json) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    // JSON文字列をMapに変換
    return mapper.readValue(json, Map.class);
  }
}
