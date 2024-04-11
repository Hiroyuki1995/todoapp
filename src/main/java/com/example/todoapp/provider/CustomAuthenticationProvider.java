package com.example.todoapp.provider;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();
    logger.info(username + 'a' + password);

    // ここでユーザー名とパスワードの検証を行う
    if (username.equals("admin") && password.equals("password")) { // TODO
      // List<GrantedAuthority> authorities = new ArrayList<>();
      // authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
      // return new UsernamePasswordAuthenticationToken(username, password,
      // authorities);
      return new UsernamePasswordAuthenticationToken(username, password,
          Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))); // 第三引数を追加することで、Authenticatedフラグがtrueになる
    } else {
      throw new BadCredentialsException("Authentication failed");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
