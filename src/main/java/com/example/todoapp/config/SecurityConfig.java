package com.example.todoapp.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
// import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestTemplate;

import com.example.todoapp.handler.CustomAuthenticationFailureHandler;
import com.example.todoapp.handler.MyAuthenticationFailureHandler;
import com.example.todoapp.handler.MyAuthenticationSuccessHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.client.registration.bizsol-mock.client-id}")
    private String clientId;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) // TODO:本当にdisabledで良いか確認
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login", "/oidc/token/*", "/oauth2/authorization/google",
                                "/login/oauth2/code/google",
                                "/google/login", "/login/*,/favicon.ico") // 認証不要なパスの定義
                        // TODO:不要なパス削除
                        .permitAll()
                        .anyRequest().authenticated()) // 上記以外のパスは全て認証必要
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // セッションIDは必要があれば作成するポリシー
                .formLogin((formLogin) -> formLogin // 独自のID/PWを使った通常のログイン
                        // ログイン処理は、provider/CustomAuthenticationProvider.javaで定義
                        // TODO: 現状 loginid: admin, password: passwordでログインするので、DBから取得するように変更
                        .loginProcessingUrl("/login") // ログインAPIのパスの定義
                        .successHandler(new MyAuthenticationSuccessHandler()) // APIとして成功時は200を返却するようにカスタマイズ。デフォルトは/へリダイレクト。
                        .failureHandler(new MyAuthenticationFailureHandler()) // APIとして失敗時は401を返却するようにカスタマイズ。デフォルトは/loginへリダイレクト。
                        .permitAll())
                // .oauth2Login(Customizer.withDefaults())
                .oauth2Login((oauth2) -> oauth2 // OAuth2.0を使って、SSOできるようにするための設定
                        .defaultSuccessUrl("http://localhost:5173", true) // ログイン成功後のリダイレクトURL
                        .failureUrl("http://localhost:5173/login") // ログイン失敗時のリダイレクトURL
                        .failureHandler(new CustomAuthenticationFailureHandler()) // カスタムハンドラーをセット
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
                                .baseUri("/oidc/token/*"))
                        // トークン取得・ユーザーデータ取得処理を行うパスの定義。このパス/{registrationId}を叩くと。この行がない場合、デフォルトは/login/oauth2/code/*（*はregistrationId
                        // 【参考】https://spring.pleiades.io/spring-security/reference/servlet/oauth2/login/advanced.html#oauth2login-advanced-redirection-endpoint
                        // // .tokenEndpoint(token -> token
                        // // .accessTokenResponseClient(this.accessTokenResponseClient()))
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(this.oidcUserService())))
                .logout((logout) -> logout
                        .logoutUrl("/logout") // ログアウトURLの定義
                        .logoutSuccessHandler(customLogoutSuccessHandler()) // APIとしてログアウト成功時は200を返却するようにスタマイズ。デフォルトは/loginへリダイレクト。
                        .deleteCookies("SESSION") // ログアウト時クッキー内のセッションIDを削除
                        .invalidateHttpSession(true) // セッションを無効にする
                        .clearAuthentication(true)) // 認証情報をクリアする
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler()) // アクセス拒否ハンドラー
                        .authenticationEntryPoint(customAuthenticationEntryPoint())); // 認証エントリーポイント

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

    // TODO:別クラスで定義した方がよいか？
    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return new OAuth2UserService<OidcUserRequest, OidcUser>() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
                if ("bizsol-mock".equals(userRequest.getClientRegistration().getRegistrationId())) {
                    // ユーザー情報の取得をカスタム処理に置き換える
                    String uri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                            .getUri();
                    OAuth2AccessToken accessToken = userRequest.getAccessToken();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(accessToken.getTokenValue());
                    HttpEntity<?> entity = new HttpEntity<>(headers);
                    OidcIdToken idToken = userRequest.getIdToken();
                    String sub = idToken.getSubject(); // ID トークンから "sub" を取得

                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.exchange(
                            uri + "?client_id=" + clientId + "&sub=" + sub,
                            HttpMethod.GET, entity, String.class);

                    try {
                        // 応答からユーザー情報を解析する
                        Map<String, Object> userInfo = new ObjectMapper().readValue(response.getBody(),
                                new TypeReference<Map<String, Object>>() {
                                });

                        // ユーザー権限の設定 (ここでは簡単な例を示します)
                        Set<GrantedAuthority> authorities = new HashSet<>();
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

                        // OIDCUser を生成する
                        OidcUserInfo oidcUserInfo = new OidcUserInfo(userInfo);
                        return new DefaultOidcUser(authorities, idToken, oidcUserInfo);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    OidcUserService defaultService = new OidcUserService();
                    return defaultService.loadUser(userRequest);
                }
            }
        };
    }
}
