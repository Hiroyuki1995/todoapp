package com.example.todoapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todoapp.exception.CustomException;

@Service
public class GoogleUserInfoService {

  // @Autowired
  // GoogleService googleService;

  private final GoogleService googleService;
  // private GoogleUserInfo googleUserinfo;

  @Autowired
  public GoogleUserInfoService(GoogleService googleService) {
    this.googleService = googleService;
    // this.googleUserinfo = new GoogleUserInfo("100453365354832393875", "おわちゃん",
    // "ちゃん", "おわ",
    // "https://lh3.googleusercontent.com/a/ACg8ocJQTH1518hKVG6nZnSCSximpoLqIVD1oDELhndGlOZ06sEKPA=s96-c",
    // "ja");
  }

  // private GoogleUserInfo googleUserinfo = new
  // GoogleUserInfo("100453365354832393875", "おわちゃん", "ちゃん", "おわ",
  // "https://lh3.googleusercontent.com/a/ACg8ocJQTH1518hKVG6nZnSCSximpoLqIVD1oDELhndGlOZ06sEKPA=s96-c",
  // "ja");

  public String fetchGoogleUserInfo(String code) throws CustomException {
    try {
      String accessToken = googleService.fetchAccessToken(code);
      String name = googleService.fetchGoogleUserInfo(accessToken);
      System.out.println("response:" + name);
      return name;
    } catch (CustomException e) {
      throw new CustomException("error", e);
    } catch (Exception e) {
      // TODO: handle exception
      throw e;
    }

  }
}
