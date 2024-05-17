package com.example.todoapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.example.todoapp.mapper.UserMapper;
import com.example.todoapp.model.User;
import com.example.todoapp.model.UserInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserInfoService {
  @Value("${spring.security.oauth2.client.provider.google.issuer-uri}")
  private String googleIssuerUri;
  @Value("${spring.security.oauth2.client.provider.line.issuer-uri}")
  private String lineIssuerUri;
  // @Value("${spring.security.oauth2.client.provider.bizsol-mock.issuer-uri}")
  // private String bizsolMockIssuerUri;

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

  public void signUp(UserInfo userInfo, HttpServletRequest request) {
    System.out.println(userInfo.getUsername() + ":" + userInfo.getPassword());
    userMapper.signUp(userInfo);

    // ※以下追加部分
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();

    if (authentication instanceof AnonymousAuthenticationToken == false) {
      SecurityContextHolder.clearContext();
    }

    try {
      request.login(userInfo.getUsername(), userInfo.getPassword());
    } catch (ServletException e) {
      e.printStackTrace();
    }
  }

  public void linkUserAccount(String loginId, OidcUser oidcUser) {
    OidcIdToken idToken = oidcUser.getIdToken();
    String issuer = idToken.getIssuer().toString();
    System.out.println("iss:" + issuer);
    System.out.println("googleIssuerUri:" + googleIssuerUri);
    if (issuer.equals(googleIssuerUri)) {
      System.out.println("Googleユーザー情報登録");
      userMapper.googleUserLink(idToken.getSubject(), idToken.getGivenName(), idToken.getFamilyName(),
          idToken.getEmail(), loginId);
    } else if (issuer.equals(lineIssuerUri)) {
      System.out.println("LINEユーザー情報登録");
      // } else if (issuer == bizsolMockIssuerUri) {
      // System.out.println("BizSOLモックユーザー情報登録");
    }
  }
}
