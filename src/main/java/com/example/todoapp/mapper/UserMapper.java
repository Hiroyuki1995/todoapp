package com.example.todoapp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.todoapp.model.User;
import com.example.todoapp.model.UserInfo;

@Mapper
public interface UserMapper {
  int authenticate(User user);

  void signUp(UserInfo userInfo);

  void googleUserLink(@Param("sub") String sub, @Param("givenName") String givenName,
      @Param("familyName") String familyName, @Param("email") String email, @Param("loginId") String loginId);
}
