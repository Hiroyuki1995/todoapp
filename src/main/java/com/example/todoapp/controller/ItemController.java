package com.example.todoapp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoapp.exception.CustomException;
import com.example.todoapp.model.Item;
import com.example.todoapp.model.UserInfo;
import com.example.todoapp.service.GoogleUserInfoService;
import com.example.todoapp.service.ItemService;
import com.example.todoapp.service.UserInfoService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ItemController {

  @Autowired
  private ItemService itemService;
  private UserInfoService userInfoService;
  private GoogleUserInfoService googleUserInfoService;

  public ItemController(ItemService itemService, UserInfoService userInfoService,
      GoogleUserInfoService googleUserInfoService) {
    this.itemService = itemService;
    this.userInfoService = userInfoService;
    this.googleUserInfoService = googleUserInfoService;
  }

  // コントローラーには/loginは記載しない

  // // TODO:おそらくSecurityConfig側に実装すべし
  // @GetMapping(value = "/google/login")
  // public void redirectGoogleAuthentication(HttpServletResponse response) throws
  // IOException {
  // // TODO:scopeは要チェック
  // response.sendRedirect(
  // "https://accounts.google.com/o/oauth2/auth?scope=profile&access_type=offline&include_granted_scope=true&response_type=code&redirect_uri=http://localhost:8081/login/oauth2/code/google&client_id=616743398611-9fesrbggfp03dptsh8d4qia0ishe8kuh.apps.googleusercontent.com");
  // }

  // // TODO:おそらくSecurityConfig側に実装すべし
  // @GetMapping(value = "/login/oauth2/code/google")
  // public void loginByGoogleAccount(HttpServletResponse response) throws
  // IOException {
  // // TODO:アクセストークンを取得＆profileを取得。OKなら、セッションをredisに入れて、
  // // set-cookieでセッションIDを返却することで、トップページに遷移できるようにする
  // response.sendRedirect("http://localhost:5173");
  // }

  @GetMapping(value = "/user")
  public UserInfo getUser() {
    return userInfoService.getUserInfo();
  }

  @GetMapping(value = "/login/oauth2/code/google")
  public void fetchGoogleUserInfo(@RequestParam("code") String code, HttpServletResponse response)
      throws IOException, CustomException {
    String name = googleUserInfoService.fetchGoogleUserInfo(code);
    response.sendRedirect("http://localhost:5173?name=" + name);
    // TODO:scopeをOIDCに変更する？
    // TODO:セッションを新たに払い出す

  }

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
    itemService.updateItem(item, itemId);
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
