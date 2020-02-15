> 기존 글은 [4.x 브랜치](https://github.com/kdevkr/spring-demo-security/tree/4.x)에서 확인하세요

## 들어가며
본 글은 스프링 시큐리티 입문을 위한 기초적인 가이드이며 스프링 시큐리티 5.2.2.RELEASE 기준의 스프링 시큐리티 공식 레퍼런스를 참고하여 작성합니다. 
만약, 이전 스프링 시큐리티 버전(5 이하)을 적용하신다면 해당 버전의 공식 레퍼런스를 참고하시기를 추천드립니다. 

- [Spring Security Reference - 5.2.2.RELEASE](https://docs.spring.io/spring-security/site/docs/5.2.2.RELEASE/reference/html5/)
- [OAuth2 Boot - 2.2.4.RELEASE](https://docs.spring.io/spring-security-oauth2-boot/docs/2.2.4.RELEASE/reference/html5/)

## 스프링 시큐리티
스프링 시큐리티를 입문하기 위해서 보안에 대한 개념과 AOP를 알고있다고 전제합니다.

### 아키텍처
먼저, 스프링 시큐리티에 대한 아키텍처는 스프링 시큐리티를 통한 보안 설정 및 입문에 도움이 됩니다.

#### 인증(Authentication)  
[스프링 시큐리티에서 인증이란 무엇인가요?](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#what-is-authentication-in-spring-security)
가장 일반적인 인증 시나리오를 생각해보면 사용자는 이름과 비밀번호를 통해 로그인을 시도하면 시스템은 사용자의 이름과 패스워드가 일치하는지 확인하고 사용자의 정보를 얻습니다. 그 후 사용자의 보안 컨텍스트를 수립하여 시스템을 사용할 수 있게 허가됩니다.

따라서, 스프링 시큐리티에서의 인증이란 사용자가 자신을 입증할 수 있는 정보를 시스템에 제공하고 시스템이 사용자에 대한 정보를 검증하고 로그인된 사용자를 위한 보안 전략을 설정하는 것을 말합니다.

> 이것은 제가 이해하는 스프링 시큐리티의 인증 개념임을 참고하세요.

#### 권한(Authorization)
스프링 시큐리티에서 권한이란 접근 제어(Access-Control)입니다. 
접근 제어란 보호된 자원에 대하여 접근을 허가하거나 거부하는 기능을 말하며 애플리케이션에서 보호된 자원은 메소드 호출이나 웹 요청이 될 수 있습니다.

> 스프링 시큐리티는 AOP 및 웹 요청을 위한 필터를 통해 접근 제어를 수행합니다.

![](https://docs.spring.io/spring-security/site/docs/current/reference/html5/images/security-interception.png)

위 그림은 보호된 자원에 대하여 권한을 확인하는데 사용되는 구현체를 설명합니다. 여러가지 구현체를 통해 다양한 방식으로 접근 제어가 가능하다는 것으로 이해하시기 바랍니다.

### 구현체
앞서 스프링 시큐리티의 인증과 권한을 이해했다면 이 두가지를 적용하기 위해서 스프링 시큐리티에 포함된 여러가지 구현체들을 알아보겠습니다.

#### AuthenticationManager, AuthenticationProvier
AuthenticationManager의 기본 구현체인 ProviderManager는 인증 요청을 자체적으로 처리하지 않고 설정된 AuthenticationProvier들에게 이를 위임합니다. 그리고 AuthenticationProvier는 차례대로 인증을 수행할 수 있는지 체크합니다.

- AnonymousAuthenticationProvider
- AbstractUserDetailsAuthenticationProvider
- DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider

#### UserDetails, GrantedAuthority
UserDetails 구현체는 사용자의 이름, 비밀번호, 부여된 권한 그리고 사용자가 활성화되었는지 아닌지에 대한 정보를 제공합니다.

> UserDetails를 구현하여 기존 사용자 정보를 인증하는데 사용할 수 있습니다.

#### UserDetailsService
UserDetailsService는 사용자가 입력한 정보(이름과 비밀번호)와 비교하여 인증을 확인하는 가장 일반적인 방법을 제공합니다.

```java UserDetailsService
UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
```

- JdbcDaoImpl

> 기본 JdbcDaoImpl는 다음의 [Security Database Schema](https://docs.spring.io/spring-security/site/docs/5.2.2.RELEASE/reference/html5/#appendix-schema)를 사용합니다.

#### PasswordEncoder
스프링 시큐리티는 비밀번호 인코딩 처리를 위한 여러가지 PasswordEncoder 구현체를 제공합니다.

- BCryptPasswordEncoder
- SCryptPasswordEncoder
- Pbkdf2PasswordEncoder
- ~~NoOpPasswordEncoder~~
- ~~StandardPasswordEncoder~~
- ~~MessageDigestPasswordEncoder~~
- ~~Md4PasswordEncoder~~
- ~~LdapShaPasswordEncoder~~

> 스프링 시큐리티 5+에서는 안전하지 않은 비밀번호 인코더를 제외했습니다.

- DelegatingPasswordEncoder

> 이 비밀번호 인코더는 여러가지 비밀번호 인코더를 연결하여 사용할 수 있는 특별한 인코더입니다.

#### Filter
스프링 시큐리티는 다음과 같은 Filter 구현체를 제공합니다.

- FilterChainProxy
- SecurityContextPersistenceFilter
- CsrfFilter
- LogoutFilter
- UsernamePasswordAuthenticationFilter
- DefaultLoginPageGeneratingFilter
- DefaultLogoutPageGeneratingFilter
- BasicAuthenticationFilter
- RequestCacheAwareFilter
- SecurityContextHolderAwareRequestFilter
- AnonymousAuthenticationFilter
- SessionManagementFilter
- ExceptionTranslationFilter
- FilterSecurityInterceptor
- ChannelProcessingFilter
- AbstractAuthenticationProcessingFilter
- AbstractPreAuthenticatedProcessingFilter
- RememberMeAuthenticationFilter
- SwitchUserFilter
- DigestAuthenticationFilter
- ConcurrentSessionFilter
- SessionManagementFilter

> org.springframework.security.web.FilterChainProxy의 로그 레벨을 DEBUG로 설정하면 요청에 대한 필터 처리 순서를 파악할 수 있습니다.

이외에도 여러가지 기능을 위한 구현체가 존재하지만 넘어가도록 하겠습니다.

### 인증 매커니즘
스프링 시큐리티는 사용자 클라이언트에서 인증 정보를 모으는 것을 인증 매커니즘이라 합니다.

사용자가 브라우저를 통해 자신을 입증할 수 있는 이름이나 비밀번호등을 제공하면 스프링 시큐리티는 인증 매커니즘을 통해 Authentication 요청 오브젝트를 만들고 AuthenticationManager에 제공합니다.

이후 인증 매커니즘은 완전히 채워진 Authentication 오브젝트를 다시 받아 (요청이 유효한 것으로 간주함) Authentication를 SecurityContextHolder에 넣어 원래 요청을 다시 시도합니다.

> 만약, AuthenticationManager에 의해 요청이 거부된 경우 인증 매커니즘은 브라우저로 다시 요청하라는 응답을 합니다.

#### SecurityContextPersistenceFilter
일반적인 웹 애플리케이션의 경우 요청 사용자 정보를 세션으로 관리합니다. 스프링 시큐리티는 SecurityContextPersistenceFilter를 통해 HTTP 요청 간에서 HttpSession에 포함된 속성들을 SecurityContext에 저장합니다. 

> 스프링 시큐리티는 세션에 대한 정보를 관리하므로 보안을 목적으로 HttpSession과 직접적으로 상호작용하면 안됩니다.

#### SecurityContextHolder
SecurityContextHolder라는 특별한 클래스는 SecurityContext를 제공하는 아주 중요한 역할을 합니다.

기본적으로 SecurityContextHolder는 ThreadLocal를 사용하여 정보를 저장하기 때문에 동일한 스레드에서 수행되는 메소드에서 SecurityContext를 항상 사용할 수 있습니다.

> SecurityContextHolder가 ThreadLocal로 동작하는 것이 올바르지 않은 경우 이를 변경할 수 있습니다.
> - SecurityContextHolder.MODE_GLOBAL
> - SecurityContextHolder.MODE_INHERITABLETHREADLOCAL
> - SecurityContextHolder.MODE_THREADLOCAL

종합적으로 요약하자면 스프링 시큐리티의 인증 매커니즘을 통해 SecurityContext에 사용자 인증 정보가 저장되어있고 SecurityContextHolder를 통해 현재 실행하고 있는 스레드에서 인증된 사용자의 정보를 가져올 수 있습니다.

## 스프링 시큐리티 예제
본 가이드는 다음과 같은 예제를 통해 여러가지 보안 기능을 적용하는 것을 설명합니다.

### [Web](web-security)
기초적인 설정 및 JDBC에 저장된 사용자 정보로 인증하는 것을 설명합니다. 추가적으로 메소드 단위의 접근 제어를 적용합니다.

### [Web with Redis](web-security-advanced)
Web 예제를 기반으로 Redis Session과 통합하여 사용하는 것을 설명합니다.

### [Web with OAuth 2.0](web-security-oauth)

### [~~Web with LDAP~~](web-security-ldap)

### [~~Reactive web~~](webflux-security)

### [~~Reactive web with OAuth 2.0~~](webflux-security-oauth)

> Taglib, SAML2, JAAS, CAS, RSocket등은 본 가이드에서는 다루지 않습니다.

## 참고
- [Spring Security Reference - 5.2.2.RELEASE](https://docs.spring.io/spring-security/site/docs/5.2.2.RELEASE/reference/html5/)
- [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture)