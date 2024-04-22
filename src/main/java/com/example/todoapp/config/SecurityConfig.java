package com.example.todoapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.example.todoapp.handler.MyAuthenticationFailureHandler;
import com.example.todoapp.handler.MyAuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) // 本当にdisabledで良いか確認
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login", "/oidc/token/*", "/oauth2/authorization/google",
                                "/login/oauth2/code/google",
                                "/google/login", "/login/*,/favicon.ico") // TODO:不要なパス削除
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin((formLogin) -> formLogin
                        .loginProcessingUrl("/login")
                        .successHandler(new MyAuthenticationSuccessHandler())
                        .failureHandler(new MyAuthenticationFailureHandler()) // カスタム失敗ハンドラー
                        .permitAll())
                // .oauth2Login(Customizer.withDefaults())
                .oauth2Login((oauth2) -> oauth2
                        .defaultSuccessUrl("http://localhost:5173", true) // ログイン成功後のリダイレクトURL
                        .failureUrl("http://localhost:5173/login") // ログイン失敗時のリダイレクトURL
                        // // .clientRegistrationRepository(clientRegistrationRepository()) //
                        // クライアント登録情報
                        // //
                        // .authorizedClientService(authorizedClientService(clientRegistrationRepository()))
                        // // 認証済みクライアントの管理
                        // // .clientRegistrationRepository(this.clientRegistrationRepository())
                        // // .authorizedClientRepository(this.authorizedClientRepository())
                        // // .authorizedClientService(this.authorizedClientService())
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/login")) // このパス/{registrationId}を叩くと認証処理が走る。デフォルトは/oauth2/authorization +
                                                    // /{registrationId}が適用される
                                                    // 【参考】https://spring.pleiades.io/spring-security/reference/servlet/oauth2/login/advanced.html#oauth2login-advanced-login-page
                        // // .authorizationRequestRepository(this.authorizationRequestRepository())
                        // // .authorizationRequestResolver(this.authorizationRequestResolver()))
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/oidc/token/*"))) //
                // トークン取得・ユーザーデータ取得処理を行うパスの定義。このパス/{registrationId}を叩くと。この行がない場合、デフォルトは/login/oauth2/code/*（*はregistrationId
                // 【参考】https://spring.pleiades.io/spring-security/reference/servlet/oauth2/login/advanced.html#oauth2login-advanced-redirection-endpoint
                // // .tokenEndpoint(token -> token
                // // .accessTokenResponseClient(this.accessTokenResponseClient()))
                // // .userInfoEndpoint(userInfo -> userInfo
                // // .userAuthoritiesMapper(this.userAuthoritiesMapper())
                // // .userService(this.oauth2UserService())
                // // .oidcUserService(this.oidcUserService())))
                // ))
                .logout((logout) -> logout
                        .logoutUrl("/logout") // ログアウトをトリガーするURL
                        .logoutSuccessHandler(customLogoutSuccessHandler()) // ログアウト成功時のハンドラ
                        .deleteCookies("SESSION") // ログアウト時にクッキーを削除
                        .invalidateHttpSession(true) // セッションを無効にする
                        .clearAuthentication(true)) // 認証情報をクリアする
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler()) // アクセス拒否ハンドラー
                        .authenticationEntryPoint(customAuthenticationEntryPoint())); // 認証エントリーポイント
        ;
        return http.build();
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            // カスタムアクセス拒否ロジック
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print("Custom access denied message");
        };
    }

    // formLoginではデフォルトが/loginページへのリダイレクトのため、401で返却するようカスタマイズ
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            // カスタム認証失敗ロジック
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("Custom unauthorized message");
            // response.setContentType("application/json;charset=UTF-8");
        };
    }

    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK ステータスを返す
        };
    }
}
