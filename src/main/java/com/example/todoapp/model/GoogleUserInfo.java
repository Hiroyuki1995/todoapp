package com.example.todoapp.model;

public class GoogleUserInfo {

  private String id;
  private String name;
  private String givenName;
  private String familyName;
  private String picture;
  private String locale;

  public GoogleUserInfo() {
  }

  public GoogleUserInfo(String id, String name, String givenName, String familyName, String picture, String locale) {
    this.id = id;
    this.name = name;
    this.givenName = givenName;
    this.familyName = familyName;
    this.picture = picture;
    this.locale = locale;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

}
