package com.example.todoapp.mapper;

// import java.util.ArrayList;
// import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.todoapp.model.Item;

@Mapper
public interface ItemMapper {
  List<Item> findAll();

  Item findOneById(@Param("itemId") String itemId);

  void addOne(Item item);

  void updateOne(Item item, @Param("itemId") String itemId);

  void deleteOneById(@Param("itemId") String itemId);
}
