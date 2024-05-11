package com.example.todoapp.mapper;

// import java.util.ArrayList;
// import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.todoapp.model.Item;

@Mapper
public interface ItemMapper {
  List<Item> findAll(String loginId);

  Item findOneById(@Param("itemId") String itemId, String loginId);
  // TODO: なぜ@Paramをいれるのか

  void addOne(Item item);

  void updateOne(Item item, @Param("itemId") String itemId);

  void deleteOneById(@Param("itemId") String itemId);
}
