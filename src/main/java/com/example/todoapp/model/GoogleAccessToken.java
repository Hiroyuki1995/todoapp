package com.example.todoapp.model;

public class GoogleAccessToken {
  private String accessToken;
  private String expiresIn;
  private String scope;
  private String tokenType;
  private String idToken;

  public GoogleAccessToken() {
  }

  public GoogleAccessToken(String accessToken, String expiresIn, String scope, String tokenType, String idToken) {
    this.accessToken = accessToken;
    this.expiresIn = expiresIn;
    this.scope = scope;
    this.tokenType = tokenType;
    this.idToken = idToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(String expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public String getIdToken() {
    return idToken;
  }

  public void setIdToken(String idToken) {
    this.idToken = idToken;
  }

}
