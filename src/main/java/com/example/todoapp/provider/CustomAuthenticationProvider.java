package com.example.todoapp.provider;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.todoapp.model.User;
import com.example.todoapp.service.UserInfoService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
  private UserInfoService userInfoService;

  @Autowired
  public CustomAuthenticationProvider(UserInfoService userInfoService) {
    this.userInfoService = userInfoService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String loginId = authentication.getName();
    String password = authentication.getCredentials().toString();
    logger.info(loginId + ":" + password);

    User user = new User(loginId, password);

    // ここでユーザー名とパスワードの検証を行う
    if (userInfoService.authenticate(user)) {
      // if(username.equals("admin") && password.equals("password")) {
      // TODO:PWを暗号化。
      // List<GrantedAuthority> authorities = new ArrayList<>();
      // authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
      // return new UsernamePasswordAuthenticationToken(username, password,
      // authorities);
      return new UsernamePasswordAuthenticationToken(loginId, password,
          Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))); // 第三引数を追加することで、Authenticatedフラグがtrueになる
      // 第一引数をStringではなく、UserDetailsを実装したカスタマイズクラスを作成することで、SecurityContextHolder.getContext().getAuthentication().getPrincipal()でその情報が取得可能。
      // TODO: ROLE_USERでよいか確認
    } else {
      throw new BadCredentialsException("Authentication failed");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
