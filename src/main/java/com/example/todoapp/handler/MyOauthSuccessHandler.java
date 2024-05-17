package com.example.todoapp.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.todoapp.service.UserInfoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * OAuthにおいて認証が成功したときの処理のカスタマイズ。
 * 独自IDでのログイン状態で、OAuth認証が成功した場合に、DBで紐付け処理を行う。
 */
public class MyOauthSuccessHandler implements AuthenticationSuccessHandler {
    private UserInfoService userInfoService;

    @Autowired
    public MyOauthSuccessHandler(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        // 元のセッション認証情報を取得
        HttpSession session = request.getSession(false);
        Authentication originalAuth = (Authentication) session.getAttribute("ORIGINAL_AUTH");

        // ORIGINAL_AUTHキーが存在する、つまり元々独自ログインIDでログインしていた場合
        if (originalAuth != null) {
            // セッションから独自アカウントログイン情報を付与する
            String loginId = originalAuth.getPrincipal().toString();
            // OIDCでのユーザー情報を取得する
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            // 既存のユーザーとOIDCユーザーの紐づけ処理を行う
            userInfoService.linkUserAccount(loginId, oidcUser);
        }
        // ログイン画面へリダイレクト
        response.sendRedirect("http://localhost:5173");
    }
}
