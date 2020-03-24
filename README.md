> 기존 초보 개발자가 이해하는 스프링 시큐리티는 [4.x 브랜치](https://github.com/kdevkr/spring-demo-security/tree/4.x)에서 확인하세요

## 들어가며
본 프로젝트는 스프링 시큐리티에 입문하는 분들에게 도움이 되고자 작성하는 문서입니다. 초보 개발자가 이해하는 스프링 시큐리티는 공식 레퍼런스를 참고하여 작성하였습니다.

- [Spring Security Reference - 5.2.2.RELEASE](https://docs.spring.io/spring-security/site/docs/5.2.2.RELEASE/reference/html5/)
- [OAuth2 Boot - 2.2.4.RELEASE](https://docs.spring.io/spring-security-oauth2-boot/docs/2.2.4.RELEASE/reference/html5/)

## 초보 개발자가 이해하는 스프링 시큐리티
스프링 시큐리티에 입문하기 위해서는 보안에 대한 개념과 스프링 AOP를 이해하고 있어야 합니다.

### 아키텍처
스프링 시큐리티 모듈을 적용하기 위해서 가장 먼저 스프링 시큐리티의 보안 아키텍처를 살펴보고 이해해여야 합니다. 보안 아키텍처를 알면 스프링 시큐리티가 동작하는 과정을 알 수 있게 됩니다.

스프링 시큐리티의 보안 아키텍처는 보안을 구성하는 두가지 영역으로 인증과 권한을 가집니다. 

#### 인증(Authentication)  
스프링 시큐리티에서 인증이란 사용자가 자신을 입증할 수 있는 정보를 시스템에 제공함으로써 시스템이 사용자에 대한 정보를 검증하고 로그인된 사용자를 위한 보안 전략을 수립하는 것을 말합니다.

> [스프링 시큐리티에서 인증이란 무엇인가요?](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#what-is-authentication-in-spring-security)

예를 들어, 가장 일반적인 인증 시나리오를 생각해보겠습니다. 사용자는 이름과 비밀번호를 통해 로그인을 시도합니다. 이에 시스템은 사용자가 입력한 이름과 비밀번호가 일치하는지를 확인하고 사용자의 정보를 얻어 시스템을 사용할 수 있도록 허가하게 됩니다.

#### 권한(Authorization)
스프링 시큐리티에서 권한이란 접근 제어(Access-Control)입니다. 

접근 제어란 보호된 자원에 대하여 접근을 허가하거나 거부하는 기능을 말하며 애플리케이션에서 보호된 자원은 메소드 호출이나 웹 요청이 될 수 있습니다.

> 스프링 시큐리티는 AOP 및 웹 요청을 위한 필터를 통해 접근 제어를 수행합니다.

![](https://docs.spring.io/spring-security/site/docs/current/reference/html5/images/security-interception.png)

위 그림은 스프링 시큐리티에서 보호된 자원에 대하여 권한을 확인하는데 사용되는 구현체를 설명합니다. 여러가지 구현체를 통해 다양한 방식으로 접근 제어가 가능하다는 것으로 이해하시기 바랍니다.

이렇게 스프링 시큐리티의 아키텍처를 종합해보면 인증과 권한을 통해 권한 승인이 필요한 부분으로 접근하기 위해서는 인증 과정을 통해 사용자(인증 주체)가 증명되어야만 한다는 것을 알 수 있습니다.

> 스프링 시큐리티의 인증과 권한에 대해서 좀 더 찾아보시기 바랍니다.

### 스프링 시큐리티 시작하기
스프링 시큐리티는 `spring-security-web`과 `spring-security-config` 모듈로 이루어져있습니다. 

> `spring-boot-starter-security`는 두 모듈에 대한 의존성을 포함합니다.

**build.gradle**  

```java
plugins {
    id 'org.springframework.boot' version '2.2.4.RELEASE'
    id 'io.spring.dependency-management' version '1.0.9.RELEASE'
    id 'java'
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-web'
}
```

#### 시큐리티 자동 구성
스프링 부트는 spring-boot-starter-security에 따라 스프링 시큐리티를 자동으로 구성하게 됩니다.

시큐리티 자동 구성 클래스는 SecurityAutoConfiguration입니다.

스프링 부트의 @EnableAutoConfiguration에 의해 SecurityAutoConfiguration가 빈으로 등록되며 이 과정에서 SpringBootWebSecurityConfiguration, WebSecurityEnablerConfiguration, SecurityDataConfiguration, SecurityProperties, AuthenticationEventPublisher가 빈으로 함께 등록됩니다.

_**SpringBootWebSecurityConfiguration**_
SpringBootWebSecurityConfiguration는 조건식에 의해 WebSecurityConfigurerAdapter를 확장한 빈이 없으면 빈으로 등록됩니다.

_**WebSecurityEnablerConfiguration**_
WebSecurityEnablerConfiguration는 WebSecurityConfigurerAdapter이 빈으로 등록되어있으면서 springSecurityFilterChain라는 이름의 빈이 존재하지않을 경우 @EnableWebSecurity를 통해 WebSecurityConfiguration를 등록합니다.

```java
/**
 * Creates the Spring Security Filter Chain
 * @return the {@link Filter} that represents the security filter chain
 * @throws Exception
 */
@Bean(name = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
public Filter springSecurityFilterChain() throws Exception {
	boolean hasConfigurers = webSecurityConfigurers != null
			&& !webSecurityConfigurers.isEmpty();
	if (!hasConfigurers) {
		WebSecurityConfigurerAdapter adapter = objectObjectPostProcessor
				.postProcess(new WebSecurityConfigurerAdapter() {
				});
		webSecurity.apply(adapter);
	}
	return webSecurity.build();
}
```

> 만약, 스프링 부트 기반이 아니라면 springSecurityFilterChain이라는 이름의 빈을 필수로 등록해야합니다.

_**SecurityDataConfiguration**_
SecurityDataConfiguration는 스프링 시큐리티와 스프링 데이터 모듈의 통합을 지원합니다.

_**SecurityFilterAutoConfiguration**_
SecurityAutoConfiguration이 자동으로 등록된 이후에 SecurityFilterAutoConfiguration가 등록되어 앞서 등록된 springSecurityFilterChain이라는 이름의 빈을 DelegatingFilterProxy에 등록합니다.

이외에도 Saml2RelyingPartyAutoConfiguration, OAuth2ResourceServerAutoConfiguration, OAuth2ClientAutoConfiguration가 조건하에 등록됩니다.

여기까지가 스프링 부트가 스프링 시큐리티에 대한 기본 구성을 적용하는 것들입니다.

## 스프링 시큐리티 구성하기
스프링 시큐리티의 인증 매커니즘에 대해 알아보고 앞서 알아본 인증과 권한을 어떻게 적용하는지 알아보겠습니다.

### 인증 매커니즘
스프링 시큐리티는 사용자 클라이언트로부터 인증 정보를 모으는 것을 인증 매커니즘이라 합니다.

사용자가 브라우저를 통해 자신을 입증할 수 있는 이름이나 비밀번호등을 제공하면 스프링 시큐리티는 인증 매커니즘을 통해 Authentication 요청 오브젝트를 만들고 AuthenticationManager에 제공합니다.

이후 인증 매커니즘은 완전히 채워진 Authentication 오브젝트를 다시 받아 (요청이 유효한 것으로 간주함) Authentication를 SecurityContextHolder에 넣어 원래 요청을 다시 시도합니다.

만약, AuthenticationManager에 의해 요청이 거부된 경우 인증 매커니즘은 브라우저로 다시 요청하라는 응답을 합니다.

### 구현체
인증 매커니즘에 대한 동작을 처리할 때 사용되는 여러가지 인터페이스 및 구현체를 제공합니다.

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
초보 개발자가 이해하는 스프링 시큐리티 예제는 스프링 부트를 기반으로 작성하였습니다. 스프링 부트는 스프링을 사용할 때 필요한 여러가지 기본 설정들을 자동으로 제공해주어 빠른 예제 작성을 위해 사용하였습니다.

### Web

#### [Web](web-security)
스프링 시큐리티 설정부터 인증 프로세스, 접근 제어를 다루는 기초 예제

#### [Web Session](web-security-session)
`spring-session` 모듈과 연계하여 인증된 사용자 정보를 Redis와 같은 분산 저장소에 저장하고 세션에 대한 동시성 제어를 적용할 수 있음을 확인하는 예제

### Web with OAuth

#### [OAuth Kakao Login](web-security-oauth-kakao-login)
`spring-security-oauth-client` 모듈을 활용하여 카카오 프로바이더에 대한 로그인을 적용할 수 있음을 확인하는 예제

#### [OAuth Server](web-security-oauth-server)
스프링 시큐리티를 통해 OAuth 2.0 시스템을 구성할 수 있음을 확인하는 예제 

## 참고
- [Spring Security Reference - 5.2.2.RELEASE](https://docs.spring.io/spring-security/site/docs/5.2.2.RELEASE/reference/html5/)
- [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture)