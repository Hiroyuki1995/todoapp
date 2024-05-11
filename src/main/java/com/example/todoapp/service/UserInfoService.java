package com.example.todoapp.service;

import org.springframework.stereotype.Service;

import com.example.todoapp.mapper.UserMapper;
import com.example.todoapp.model.User;
import com.example.todoapp.model.UserInfo;

@Service
public class UserInfoService {

  // @Autowired
  private final UserMapper userMapper;

  // public UserInfoService() {
  // }

  public UserInfoService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  private UserInfo userinfo = new UserInfo("admin");

  // TODO: ちゃんとレスポンスを返却する
  public UserInfo getUserInfo() {
    return userinfo;
  }

  public Boolean authenticate(User user) {
    return userMapper.authenticate(user) != 0;
  }
}
