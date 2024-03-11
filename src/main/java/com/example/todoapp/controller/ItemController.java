package com.example.todoapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoapp.model.Item;
import com.example.todoapp.service.ItemService;

@RestController
public class ItemController {

  @Autowired
  private ItemService itemService;

  @GetMapping(value = "/items")
  public List<Item> getAllItems() {
    return itemService.getAllItems();
  }

  @GetMapping("/items/{itemId}")
  public Item getItem(@PathVariable("itemId") String itemId) {
    return itemService.getItem(itemId);
  }

  @PostMapping("/items")
  public void addItem(@RequestBody Item item) {
    itemService.addItem(item);
  }

  @PutMapping("/items/{itemId}")
  public void updateItem(@RequestBody Item item, @PathVariable("itemId") String itemId) {
    itemService.udpateItem(item, itemId);
  }

  @DeleteMapping("/items/{itemId}")
  public void deleteItem(@PathVariable("itemId") String itemId) {
    itemService.deleteItem(itemId);
  }

  @GetMapping("/")
  public String index() {
    return "アクセス成功です";
  }

  @GetMapping(value = "/welcome")
  public String welcome() {
    return "Spring Bootへようこそ";
  }

  @GetMapping(value = "/hello")
  public String hello() {
    return "Hello World!";
  }

}
