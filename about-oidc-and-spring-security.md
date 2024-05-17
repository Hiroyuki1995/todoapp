# OpenID Connect について

## OIDC の設定ファイルは一般的に以下の URL に記載。認可エンドポイント・トークンエンドポイント・JWKS エンドポイント（公開鍵情報など）などが定義される。

{URL}/.well-known/openid-configuration

# spring security について

## 設定ファイルのメインは以下

- /config/SecurityConfig.java
- XML 形式 でも代替可能。

## OAuth2.0 のクライアント・登録情報は application.properties で以下のように要定義。

### ※但し、Spring Security では、client-id や client-secret 以外の情報は、issuer-uri だけ定義すれば、ライブラリ側が/.well-known/openid-configuration に自動で問い合わせるため、省略を不要にできる模様（詳細は要確認）

### OIDC によって取得した ID トークンは、jwk-set-uri を使って自動で検証をしてくれる模様（詳細は要確認）

- spring.security.oauth2.client.registration.line.client-id
- spring.security.oauth2.client.registration.line.client-secret
- spring.security.oauth2.client.registration.line.client-authentication-method
- spring.security.oauth2.client.registration.line.authorization-grant-type
- spring.security.oauth2.client.registration.line.redirect-uri
  - デフォルトは{baseUrl}login/oauth2/code/{registrationId e.g,google}
- spring.security.oauth2.client.registration.line.scope
- spring.security.oauth2.client.registration.line.provider
- spring.security.oauth2.client.provider.line.user-name-attribute
- spring.security.oauth2.client.registration.line.client-name
- spring.security.oauth2.client.provider.line.authorization-uri
- spring.security.oauth2.client.provider.line.token-uri
- spring.security.oauth2.client.provider.line.user-info-uri
- spring.security.oauth2.client.provider.line.jwk-set-uri

### クライアント側のデフォルト値

- 認可エンドポイントへ自動リクエストをするエンドポイント:/oauth2/authorization/{registrationId e.g,google}
- この値は SecurityConfig.java にて定義する

## デバッグのために以下を定義すると良い

- logging.level.org.springframework.security=debug
- logging.level.org.springframework.web=DEBUG
- logging.level.org.springframework.web.client.RestTemplate=DEBUG
- logging.level.org.springframework.http.client.HttpClient=DEBUG
- logging.level.org.springframework.web.reactive.function.client.WebClient=DEBUG

## Google OAuth を使った、SSO 時のバックエンドのログ（機密情報は\*でマスキング）

```
Request received for GET '/login/google':

org.springframework.session.web.http.SessionRepositoryFilter$SessionRepositoryRequestWrapper@3137b6dc

servletPath:/login/google
pathInfo:null
headers:
host: localhost:8081
connection: keep-alive
pragma: no-cache
cache-control: no-cache
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "macOS"
upgrade-insecure-requests: 1
user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
sec-fetch-site: same-site
sec-fetch-mode: navigate
sec-fetch-user: ?1
sec-fetch-dest: document
referer: http://localhost:5173/
accept-encoding: gzip, deflate, br, zstd
accept-language: en-US,en;q=0.9,ja;q=0.8
cookie: SESSION=YzBkMjhlNjQtN2NlYS00MzA0LTkzOGItYjE2MWFiNDlkODEz


Security filter chain: [
  DisableEncodeUrlFilter
  WebAsyncManagerIntegrationFilter
  SecurityContextHolderFilter
  HeaderWriterFilter
  CorsFilter
  LogoutFilter
  OAuth2AuthorizationRequestRedirectFilter
  OAuth2LoginAuthenticationFilter
  UsernamePasswordAuthenticationFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
  AuthorizationFilter
]


************************************************************


2024-04-22T14:27:36.069+09:00 DEBUG 41032 --- [nio-8081-exec-1] o.s.security.web.FilterChainProxy        : Securing GET /login/google
2024-04-22T14:27:36.827+09:00  INFO 41032 --- [nio-8081-exec-1] Spring Security Debugger                 :

************************************************************

New HTTP session created: 8c8637e9-77f7-4f9a-b44d-805af94f62b0

Call stack:

        at org.springframework.security.web.debug.Logger.info(Logger.java:46)
        at org.springframework.security.web.debug.DebugFilter$DebugRequestWrapper.getSession(DebugFilter.java:171)
        at jakarta.servlet.http.HttpServletRequestWrapper.getSession(HttpServletRequestWrapper.java:229)
        at jakarta.servlet.http.HttpServletRequestWrapper.getSession(HttpServletRequestWrapper.java:229)
        at org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository.saveAuthorizationRequest(HttpSessionOAuth2AuthorizationRequestRepository.java:69)
        at org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.sendRedirectForAuthorization(OAuth2AuthorizationRequestRedirectFilter.java:219)
        at org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.doFilterInternal(OAuth2AuthorizationRequestRedirectFilter.java:172)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:107)
        at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:93)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:91)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90)
        at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.context.SecurityContextHolderFilter.doFilter(SecurityContextHolderFilter.java:82)
        at org.springframework.security.web.context.SecurityContextHolderFilter.doFilter(SecurityContextHolderFilter.java:69)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:62)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.session.DisableEncodeUrlFilter.doFilterInternal(DisableEncodeUrlFilter.java:42)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:233)
        at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:191)
        at org.springframework.security.web.debug.DebugFilter.invokeWithWrappedRequest(DebugFilter.java:90)
        at org.springframework.security.web.debug.DebugFilter.doFilter(DebugFilter.java:78)
        at org.springframework.security.web.debug.DebugFilter.doFilter(DebugFilter.java:67)
        at org.springframework.web.filter.CompositeFilter$VirtualFilterChain.doFilter(CompositeFilter.java:113)
        at org.springframework.web.servlet.handler.HandlerMappingIntrospector.lambda$createCacheFilter$3(HandlerMappingIntrospector.java:195)
        at org.springframework.web.filter.CompositeFilter$VirtualFilterChain.doFilter(CompositeFilter.java:113)
        at org.springframework.web.filter.CompositeFilter.doFilter(CompositeFilter.java:74)
        at org.springframework.security.config.annotation.web.configuration.WebMvcSecurityConfiguration$CompositeFilterChainProxy.doFilter(WebMvcSecurityConfiguration.java:230)
        at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:352)
        at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:268)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
        at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
        at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
        at org.springframework.session.web.http.SessionRepositoryFilter.doFilterInternal(SessionRepositoryFilter.java:142)
        at org.springframework.session.web.http.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:82)
        at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:352)
        at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:268)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
        at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
        at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)
        at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)
        at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482)
        at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115)
        at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)
        at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
        at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:344)
        at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:391)
        at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
        at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:896)
        at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1744)
        at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
        at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
        at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)
        at java.base/java.lang.Thread.run(Thread.java:833)


************************************************************


2024-04-22T14:27:36.829+09:00 DEBUG 41032 --- [nio-8081-exec-1] o.s.s.web.DefaultRedirectStrategy        : Redirecting to https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=616743398611-9fesrbggfp03dptsh8d4qia0ishe8kuh.apps.googleusercontent.com&scope=openid%20email%20profile&state=alo_sd9HkR84VIaw02QwHsChhwpSfUsaJd17mqEletY%3D&redirect_uri=http://localhost:8081/oidc/token/google&nonce=*******************
2024-04-22T14:27:37.376+09:00  INFO 41032 --- [nio-8081-exec-2] Spring Security Debugger                 :

************************************************************

Request received for GET '/oidc/token/google?state=alo_sd9HkR84VIaw02QwHsChhwpSfUsaJd17mqEletY%3D&code=***********&scope=email+profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile&authuser=0&prompt=none':

org.springframework.session.web.http.SessionRepositoryFilter$SessionRepositoryRequestWrapper@7197554

servletPath:/oidc/token/google
pathInfo:null
headers:
host: localhost:8081
connection: keep-alive
pragma: no-cache
cache-control: no-cache
upgrade-insecure-requests: 1
user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
sec-fetch-site: cross-site
sec-fetch-mode: navigate
sec-fetch-user: ?1
sec-fetch-dest: document
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "macOS"
referer: http://localhost:5173/
accept-encoding: gzip, deflate, br, zstd
accept-language: en-US,en;q=0.9,ja;q=0.8
cookie: SESSION=OGM4NjM3ZTktNzdmNy00ZjlhLWI0NGQtODA1YWY5NGY2MmIw


Security filter chain: [
  DisableEncodeUrlFilter
  WebAsyncManagerIntegrationFilter
  SecurityContextHolderFilter
  HeaderWriterFilter
  CorsFilter
  LogoutFilter
  OAuth2AuthorizationRequestRedirectFilter
  OAuth2LoginAuthenticationFilter
  UsernamePasswordAuthenticationFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
  AuthorizationFilter
]


************************************************************


2024-04-22T14:27:37.387+09:00 DEBUG 41032 --- [nio-8081-exec-2] o.s.security.web.FilterChainProxy        : Securing GET /oidc/token/google?state=alo_sd9HkR84VIaw02QwHsChhwpSfUsaJd17mqEletY%3D&code=***********&scope=email+profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile&authuser=0&prompt=none
2024-04-22T14:27:37.421+09:00 DEBUG 41032 --- [nio-8081-exec-2] o.s.web.client.RestTemplate              : HTTP POST https://oauth2.googleapis.com/token
2024-04-22T14:27:37.421+09:00 DEBUG 41032 --- [nio-8081-exec-2] o.s.web.client.RestTemplate              : Accept=[application/json, application/*+json]
2024-04-22T14:27:37.425+09:00 DEBUG 41032 --- [nio-8081-exec-2] o.s.web.client.RestTemplate              : Writing [{grant_type=[authorization_code], code=[***********], redirect_uri=[http://localhost:8081/oidc/token/google]}] as "application/x-www-form-urlencoded;charset=UTF-8"
2024-04-22T14:27:37.767+09:00 DEBUG 41032 --- [nio-8081-exec-2] o.s.web.client.RestTemplate              : Response 200 OK
2024-04-22T14:27:37.768+09:00 DEBUG 41032 --- [nio-8081-exec-2] o.s.web.client.RestTemplate              : Reading to [org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse] as "application/json;charset=utf-8"
2024-04-22T14:27:37.872+09:00 DEBUG 41032 --- [nio-8081-exec-2] o.s.web.client.RestTemplate              : HTTP GET https://www.googleapis.com/oauth2/v3/certs
2024-04-22T14:27:37.873+09:00 DEBUG 41032 --- [nio-8081-exec-2] o.s.web.client.RestTemplate              : Accept=[text/plain, application/json, application/*+json, */*]
2024-04-22T14:27:38.092+09:00 DEBUG 41032 --- [nio-8081-exec-2] o.s.web.client.RestTemplate              : Response 200 OK
2024-04-22T14:27:38.093+09:00 DEBUG 41032 --- [nio-8081-exec-2] o.s.web.client.RestTemplate              : Reading to [java.lang.String] as "application/json;charset=UTF-8"
2024-04-22T14:27:38.114+09:00 DEBUG 41032 --- [nio-8081-exec-2] .s.ChangeSessionIdAuthenticationStrategy : Changed session id from 8c8637e9-77f7-4f9a-b44d-805af94f62b0
2024-04-22T14:27:38.116+09:00 DEBUG 41032 --- [nio-8081-exec-2] w.c.HttpSessionSecurityContextRepository : Stored SecurityContextImpl [Authentication=OAuth2AuthenticationToken [Principal=Name: [************], Granted Authorities: [[OIDC_USER, SCOPE_https://www.googleapis.com/auth/userinfo.email, SCOPE_https://www.googleapis.com/auth/userinfo.profile, SCOPE_openid]], User Attributes: [{at_hash=*******************, sub=************, email_verified=true, iss=https://accounts.google.com, given_name=***, nonce==*******************, picture=https://lh3.googleusercontent.com/a/ACg8ocJQTH1518hKVG6nZnSCSximpoLqIVD1oDELhndGlOZ06sEKPA=s96-c, aud=[616743398611-9fesrbggfp03dptsh8d4qia0ishe8kuh.apps.googleusercontent.com], azp=616743398611-9fesrbggfp03dptsh8d4qia0ishe8kuh.apps.googleusercontent.com, name=*****, exp=2024-04-22T06:27:37Z, family_name=**, iat=2024-04-22T05:27:37Z, email=********@gmail.com}], Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=8c8637e9-77f7-4f9a-b44d-805af94f62b0], Granted Authorities=[OIDC_USER, SCOPE_https://www.googleapis.com/auth/userinfo.email, SCOPE_https://www.googleapis.com/auth/userinfo.profile, SCOPE_openid]]] to HttpSession [org.springframework.session.web.http.SessionRepositoryFilter$SessionRepositoryRequestWrapper$HttpSessionWrapper@2fbf901c]
2024-04-22T14:27:38.116+09:00 DEBUG 41032 --- [nio-8081-exec-2] .s.o.c.w.OAuth2LoginAuthenticationFilter : Set SecurityContextHolder to OAuth2AuthenticationToken [Principal=Name: [************], Granted Authorities: [[OIDC_USER, SCOPE_https://www.googleapis.com/auth/userinfo.email, SCOPE_https://www.googleapis.com/auth/userinfo.profile, SCOPE_openid]], User Attributes: [{at_hash=************, sub=************, email_verified=true, iss=https://accounts.google.com, given_name=***, nonce==*******************, picture=https://lh3.googleusercontent.com/a/ACg8ocJQTH1518hKVG6nZnSCSximpoLqIVD1oDELhndGlOZ06sEKPA=s96-c, aud=[616743398611-9fesrbggfp03dptsh8d4qia0ishe8kuh.apps.googleusercontent.com], azp=616743398611-9fesrbggfp03dptsh8d4qia0ishe8kuh.apps.googleusercontent.com, name=*****, exp=2024-04-22T06:27:37Z, family_name=**, iat=2024-04-22T05:27:37Z, email=********@gmail.com}], Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=8c8637e9-77f7-4f9a-b44d-805af94f62b0], Granted Authorities=[OIDC_USER, SCOPE_https://www.googleapis.com/auth/userinfo.email, SCOPE_https://www.googleapis.com/auth/userinfo.profile, SCOPE_openid]]
2024-04-22T14:27:38.116+09:00 DEBUG 41032 --- [nio-8081-exec-2] o.s.s.web.DefaultRedirectStrategy        : Redirecting to http://localhost:5173
2024-04-22T14:27:39.358+09:00  INFO 41032 --- [nio-8081-exec-3] Spring Security Debugger                 :

************************************************************

Request received for GET '/user':

org.springframework.session.web.http.SessionRepositoryFilter$SessionRepositoryRequestWrapper@202ab54c

servletPath:/user
pathInfo:null
headers:
cookie: SESSION=YWNjZGU3M2EtMzYwYi00ZTUzLWEzOTMtNTdiNmYwMmVlMTZi
accept-language: en-US,en;q=0.9,ja;q=0.8
accept-encoding: gzip, deflate, br, zstd
referer: http://localhost:5173/
sec-fetch-dest: empty
sec-fetch-mode: cors
sec-fetch-site: same-origin
accept: */*
sec-ch-ua-platform: "macOS"
user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-mobile: ?0
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
cache-control: no-cache
pragma: no-cache
connection: close
host: localhost:8081


Security filter chain: [
  DisableEncodeUrlFilter
  WebAsyncManagerIntegrationFilter
  SecurityContextHolderFilter
  HeaderWriterFilter
  CorsFilter
  LogoutFilter
  OAuth2AuthorizationRequestRedirectFilter
  OAuth2LoginAuthenticationFilter
  UsernamePasswordAuthenticationFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
  AuthorizationFilter
]


************************************************************


2024-04-22T14:27:39.359+09:00 DEBUG 41032 --- [nio-8081-exec-3] o.s.security.web.FilterChainProxy        : Securing GET /user
2024-04-22T14:27:39.369+09:00 DEBUG 41032 --- [nio-8081-exec-3] w.c.HttpSessionSecurityContextRepository : Retrieved SecurityContextImpl [Authentication=OAuth2AuthenticationToken [Principal=Name: [************], Granted Authorities: [[OIDC_USER, SCOPE_https://www.googleapis.com/auth/userinfo.email, SCOPE_https://www.googleapis.com/auth/userinfo.profile, SCOPE_openid]], User Attributes: [{at_hash=************, sub=************, email_verified=true, iss=https://accounts.google.com, given_name=***, nonce==*******************, picture=https://lh3.googleusercontent.com/a/ACg8ocJQTH1518hKVG6nZnSCSximpoLqIVD1oDELhndGlOZ06sEKPA=s96-c, aud=[616743398611-9fesrbggfp03dptsh8d4qia0ishe8kuh.apps.googleusercontent.com], azp=616743398611-9fesrbggfp03dptsh8d4qia0ishe8kuh.apps.googleusercontent.com, name=*****, exp=2024-04-22T06:27:37Z, family_name=**, iat=2024-04-22T05:27:37Z, email=********@gmail.com}], Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=8c8637e9-77f7-4f9a-b44d-805af94f62b0], Granted Authorities=[OIDC_USER, SCOPE_https://www.googleapis.com/auth/userinfo.email, SCOPE_https://www.googleapis.com/auth/userinfo.profile, SCOPE_openid]]]
2024-04-22T14:27:39.370+09:00 DEBUG 41032 --- [nio-8081-exec-3] o.s.security.web.FilterChainProxy        : Secured GET /user
2024-04-22T14:27:39.372+09:00 DEBUG 41032 --- [nio-8081-exec-3] o.s.web.servlet.DispatcherServlet        : GET "/user", parameters={}
2024-04-22T14:27:39.372+09:00 DEBUG 41032 --- [nio-8081-exec-3] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped to com.example.todoapp.controller.ItemController#getUser()
2024-04-22T14:27:39.517+09:00 DEBUG 41032 --- [nio-8081-exec-3] m.m.a.RequestResponseBodyMethodProcessor : Using 'application/json', given [*/*] and supported [application/json, application/*+json]
2024-04-22T14:27:39.519+09:00 DEBUG 41032 --- [nio-8081-exec-3] m.m.a.RequestResponseBodyMethodProcessor : Writing [com.example.todoapp.model.UserInfo@6bb93930]
2024-04-22T14:27:39.535+09:00 DEBUG 41032 --- [nio-8081-exec-3] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
```

## 【参考】独自ログイン ID/PW 時のログ

```
Request received for POST '/login':

org.springframework.session.web.http.SessionRepositoryFilter$SessionRepositoryRequestWrapper@42c974c8

servletPath:/login
pathInfo:null
headers:
accept-language: en-US,en;q=0.9,ja;q=0.8
accept-encoding: gzip, deflate, br, zstd
referer: http://localhost:5173/login
sec-fetch-dest: empty
sec-fetch-mode: cors
sec-fetch-site: same-origin
origin: http://localhost:5173
accept: */*
content-type: application/x-www-form-urlencoded
user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "macOS"
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
cache-control: no-cache
pragma: no-cache
content-length: 32
connection: close
host: localhost:8081


Security filter chain: [
  DisableEncodeUrlFilter
  WebAsyncManagerIntegrationFilter
  SecurityContextHolderFilter
  HeaderWriterFilter
  CorsFilter
  LogoutFilter
  OAuth2AuthorizationRequestRedirectFilter
  OAuth2LoginAuthenticationFilter
  UsernamePasswordAuthenticationFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
  AuthorizationFilter
]


************************************************************


2024-04-22T14:49:01.871+09:00 DEBUG 41032 --- [nio-8081-exec-1] o.s.security.web.FilterChainProxy        : Securing POST /login
2024-04-22T14:49:01.880+09:00  INFO 41032 --- [nio-8081-exec-1] c.e.t.p.CustomAuthenticationProvider     : adminapassword
2024-04-22T14:49:01.883+09:00  INFO 41032 --- [nio-8081-exec-1] Spring Security Debugger                 :

************************************************************

New HTTP session created: d96b5646-9951-4f06-a76a-249296358a4e

Call stack:

        at org.springframework.security.web.debug.Logger.info(Logger.java:46)
        at org.springframework.security.web.debug.DebugFilter$DebugRequestWrapper.getSession(DebugFilter.java:171)
        at org.springframework.security.web.debug.DebugFilter$DebugRequestWrapper.getSession(DebugFilter.java:181)
        at jakarta.servlet.http.HttpServletRequestWrapper.getSession(HttpServletRequestWrapper.java:221)
        at jakarta.servlet.http.HttpServletRequestWrapper.getSession(HttpServletRequestWrapper.java:221)
        at org.springframework.security.web.context.HttpSessionSecurityContextRepository.saveContextInHttpSession(HttpSessionSecurityContextRepository.java:169)
        at org.springframework.security.web.context.HttpSessionSecurityContextRepository.saveContext(HttpSessionSecurityContextRepository.java:152)
        at org.springframework.security.web.context.DelegatingSecurityContextRepository.saveContext(DelegatingSecurityContextRepository.java:77)
        at org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.successfulAuthentication(AbstractAuthenticationProcessingFilter.java:325)
        at org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter(AbstractAuthenticationProcessingFilter.java:241)
        at org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter(AbstractAuthenticationProcessingFilter.java:221)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter(AbstractAuthenticationProcessingFilter.java:227)
        at org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter(AbstractAuthenticationProcessingFilter.java:221)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.doFilterInternal(OAuth2AuthorizationRequestRedirectFilter.java:181)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:107)
        at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:93)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:91)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90)
        at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.context.SecurityContextHolderFilter.doFilter(SecurityContextHolderFilter.java:82)
        at org.springframework.security.web.context.SecurityContextHolderFilter.doFilter(SecurityContextHolderFilter.java:69)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:62)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.session.DisableEncodeUrlFilter.doFilterInternal(DisableEncodeUrlFilter.java:42)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)
        at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:233)
        at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:191)
        at org.springframework.security.web.debug.DebugFilter.invokeWithWrappedRequest(DebugFilter.java:90)
        at org.springframework.security.web.debug.DebugFilter.doFilter(DebugFilter.java:78)
        at org.springframework.security.web.debug.DebugFilter.doFilter(DebugFilter.java:67)
        at org.springframework.web.filter.CompositeFilter$VirtualFilterChain.doFilter(CompositeFilter.java:113)
        at org.springframework.web.servlet.handler.HandlerMappingIntrospector.lambda$createCacheFilter$3(HandlerMappingIntrospector.java:195)
        at org.springframework.web.filter.CompositeFilter$VirtualFilterChain.doFilter(CompositeFilter.java:113)
        at org.springframework.web.filter.CompositeFilter.doFilter(CompositeFilter.java:74)
        at org.springframework.security.config.annotation.web.configuration.WebMvcSecurityConfiguration$CompositeFilterChainProxy.doFilter(WebMvcSecurityConfiguration.java:230)
        at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:352)
        at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:268)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
        at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
        at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
        at org.springframework.session.web.http.SessionRepositoryFilter.doFilterInternal(SessionRepositoryFilter.java:142)
        at org.springframework.session.web.http.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:82)
        at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:352)
        at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:268)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
        at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
        at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)
        at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)
        at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482)
        at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115)
        at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)
        at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
        at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:344)
        at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:391)
        at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
        at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:896)
        at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1744)
        at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
        at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
        at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)
        at java.base/java.lang.Thread.run(Thread.java:833)


************************************************************


2024-04-22T14:49:01.899+09:00 DEBUG 41032 --- [nio-8081-exec-1] w.c.HttpSessionSecurityContextRepository : Stored SecurityContextImpl [Authentication=UsernamePasswordAuthenticationToken [Principal=admin, Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[ROLE_USER]]] to HttpSession [org.springframework.session.web.http.SessionRepositoryFilter$SessionRepositoryRequestWrapper$HttpSessionWrapper@112bc1c2]
2024-04-22T14:49:01.899+09:00 DEBUG 41032 --- [nio-8081-exec-1] w.a.UsernamePasswordAuthenticationFilter : Set SecurityContextHolder to UsernamePasswordAuthenticationToken [Principal=admin, Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[ROLE_USER]]
2024-04-22T14:49:03.201+09:00  INFO 41032 --- [nio-8081-exec-2] Spring Security Debugger                 :

************************************************************

Request received for GET '/user':

org.springframework.session.web.http.SessionRepositoryFilter$SessionRepositoryRequestWrapper@55747790

servletPath:/user
pathInfo:null
headers:
cookie: SESSION=ZDk2YjU2NDYtOTk1MS00ZjA2LWE3NmEtMjQ5Mjk2MzU4YTRl
accept-language: en-US,en;q=0.9,ja;q=0.8
accept-encoding: gzip, deflate, br, zstd
referer: http://localhost:5173/
sec-fetch-dest: empty
sec-fetch-mode: cors
sec-fetch-site: same-origin
accept: */*
sec-ch-ua-platform: "macOS"
user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
sec-ch-ua-mobile: ?0
sec-ch-ua: "Chromium";v="124", "Google Chrome";v="124", "Not-A.Brand";v="99"
cache-control: no-cache
pragma: no-cache
connection: close
host: localhost:8081
```
