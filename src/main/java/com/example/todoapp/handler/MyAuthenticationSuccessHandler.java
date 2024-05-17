package com.example.todoapp.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        // 認証成功時の処理。ここでは単に200 OKステータスを設定しています。
        response.setStatus(HttpServletResponse.SC_OK);
        request.getSession().setAttribute("ORIGINAL_AUTH", authentication);

        // 必要に応じて、レスポンスボディに情報を追加することもできます。
        // response.getWriter().print("{\"message\": \"Authentication successful\"}");
        // response.getWriter().flush();
    }
}
