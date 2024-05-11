package com.example.todoapp.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BizsolMockUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws RuntimeException {
    
    // クエリパラメータ用にクライアントIDを取得（ここでは"bizsol-mock"固定値）
    String clientId = userRequest.getClientRegistration().getClientId();
    // ID トークンから "sub" を取得
    OidcIdToken idToken = userRequest.getIdToken();
    String sub = idToken.getSubject();
    // クエリパラメータを付与したエンドポイントを生成
    String uri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri() + "?client_id=" + clientId + "&sub=" + sub;

    // ヘッダのAuthorizationに、Bearerとしてアクセストークンを設定（Spring Securityデフォルトでも実施しているが、ここでは明示的に設定が必要）
    OAuth2AccessToken accessToken = userRequest.getAccessToken();
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken.getTokenValue());
    HttpEntity<?> entity = new HttpEntity<>(headers);
    
    // 上記のURL・ヘッダ情報をもとにリクエストを投げる
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(uri,
        HttpMethod.GET, entity, String.class);

    try {
      Map<String, Object> userInfo = new ObjectMapper().readValue(response.getBody(),
          new TypeReference<Map<String, Object>>() {
          });
      Set<GrantedAuthority> authorities = new HashSet<>();
      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

      OidcUserInfo oidcUserInfo = new OidcUserInfo(userInfo);
      return new DefaultOidcUser(authorities, idToken, oidcUserInfo);
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse user info", e);
    }
  }
}
