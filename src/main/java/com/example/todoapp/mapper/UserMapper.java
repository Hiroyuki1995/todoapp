package com.example.todoapp.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.todoapp.model.User;

@Mapper
public interface UserMapper {
  int authenticate(User user);
}
