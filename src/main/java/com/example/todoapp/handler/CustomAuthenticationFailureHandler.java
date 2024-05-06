package com.example.todoapp.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException {
    if (exception instanceof OAuth2AuthenticationException) {
      OAuth2AuthenticationException oauthException = (OAuth2AuthenticationException) exception;
      // ここで詳細なエラーログを出力
      System.err.println("OAuth2 Authentication failed: " + oauthException.getError().toString());
      System.err.println("Detailed error description: " + oauthException.getError().getDescription());
    }

    // オリジナルのエラーレスポンスを処理
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
  }
}
