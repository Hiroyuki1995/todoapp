package com.example.todoapp.model;

public class UserInfo {

  private String username;
  private String phonenumber;
  private Boolean isValid;

  public UserInfo() {
  }

  public UserInfo(String username, String phonenumber, Boolean isValid) {
    this.username = username;
    this.phonenumber = phonenumber;
    this.isValid = isValid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPhonenumber() {
    return phonenumber;
  }

  public void setPhonenumber(String phonenumber) {
    this.phonenumber = phonenumber;
  }

  public Boolean getIsValid() {
    return isValid;
  }

  public void setIsValid(Boolean isValid) {
    this.isValid = isValid;
  }

}
