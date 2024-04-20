package com.example.todoapp.service;

import org.springframework.stereotype.Service;

// import com.example.todoapp.mapper.UserInfoMapper;
import com.example.todoapp.model.UserInfo;

@Service
public class UserInfoService {
  private UserInfo userinfo = new UserInfo("admin", "0120999999", true);

  public UserInfo getUserInfo() {
    return userinfo;
  }
}
