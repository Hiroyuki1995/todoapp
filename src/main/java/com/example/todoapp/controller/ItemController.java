package com.example.todoapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoapp.model.Item;
import com.example.todoapp.model.UserInfo;
import com.example.todoapp.service.ItemService;
import com.example.todoapp.service.UserInfoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class ItemController {

  @Autowired
  private ItemService itemService;
  private UserInfoService userInfoService;

  public ItemController(ItemService itemService, UserInfoService userInfoService) {
    this.itemService = itemService;
    this.userInfoService = userInfoService;
  }

  // コントローラーには/loginは記載しない

  @ModelAttribute("loginId")
  public String getCurrentLoginId(HttpSession session) {
    Authentication authentication = (Authentication) session.getAttribute("ORIGINAL_AUTH");
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();
      // CustomAuthenticationProviderのUsernamePasswordAuthenticationTokenの第一引数で定義した値
      System.out.println("principal:" + principal);
      if (principal instanceof String) {
        return (String) principal;
      }
    }
    return null;
  }

  @PostMapping(value = "/signup")
  public void signUp(@RequestBody UserInfo userInfo, HttpServletRequest request) {
    userInfoService.signUp(userInfo, request);
  }

  @GetMapping(value = "/user")
  public UserInfo getUser() {
    return userInfoService.getUserInfo();
  }

  @GetMapping(value = "/items")
  public List<Item> getAllItems(@ModelAttribute("loginId") String loginId) {
    return itemService.getAllItems(loginId);
  }

  @GetMapping("/items/{itemId}")
  public Item getItem(@PathVariable("itemId") String itemId, @ModelAttribute("loginId") String loginId) {
    return itemService.getItem(itemId, loginId);
  }

  @PostMapping("/items")
  public void addItem(@RequestBody Item item, @ModelAttribute("loginId") String loginId) { // item.itemIdはnull
    itemService.addItem(item, loginId);
  }

  @PutMapping("/items/{itemId}")
  public void updateItem(@RequestBody Item item, @PathVariable("itemId") String itemId) {
    itemService.updateItem(item, itemId);
  }

  @DeleteMapping("/items/{itemId}")
  public void deleteItem(@PathVariable("itemId") String itemId) {
    itemService.deleteItem(itemId);
  }
}
