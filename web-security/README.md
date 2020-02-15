## Web Security
본 프로젝트에서는 스프링 시큐리티 모듈에 대한 적용을 설명합니다.

- Spring Security 5.2.2.RELEASE
- Spring Boot 2.2.4.RELEASE

### SecurityAutoConfiguration
본 예제는 스프링 부트를 기반으로 되어있기 때문에 기본적인 스프링 시큐리티를 자동으로 적용하고 있습니다. SecurityAutoConfiguration 클래스를 살펴보면 스프링 부트가 어떻게 스프링 시큐리티를 사용할 수 있게 해주는지 알 수 있습니다.

```java
@Configuration(proxyBeanMethods = false)
@Import({ SpringBootWebSecurityConfiguration.class, WebSecurityEnablerConfiguration.class,
		SecurityDataConfiguration.class })
public class SecurityAutoConfiguration {}
```

- SpringBootWebSecurityConfiguration : WebSecurityConfigurerAdapter 유형의 빈이 없을 경우 DefaultConfigurerAdapter를 등록합니다.
- WebSecurityEnablerConfiguration : WebSecurityConfigurerAdapter 유형의 빈이 있는데 springSecurityFilterChain 빈이 없을 경우 @EnableWebSecurity를 적용합니다.
- SecurityDataConfiguration : 스프링 데이터 모듈과의 통합을 제공합니다.

### 커스터마이징
일반적으로 프로젝트 또는 애플리케이션마다 보안에 대한 요구사항이 상이하므로 스프링 시큐리티에 대한 설정을 커스터마이징해야합니다. 스프링 부트 기본 시큐리티 설정이 아닌 우리만의 시큐리티 설정으로 변경해봅시다.

#### PasswordEncoder
DelegatingPasswordEncoder 구현체를 사용하면 여러가지 패스워드 인코더를 연결하여 사용할 수 있습니다.

```java
@Bean
public PasswordEncoder passwordEncoder() {
    String idForEncode = "bcrypt";

    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put(idForEncode, new BCryptPasswordEncoder(12));
    encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
    encoders.put("scrypt", new SCryptPasswordEncoder());

    return new DelegatingPasswordEncoder(idForEncode, encoders);
}
```

> 스프링 부트는 기본 패스워드 인코더로 BCryptPasswordEncoder를 사용합니다.

#### SecurityConfig.WebSecurityConfig
WebSecurityConfigurerAdapter 유형의 빈을 등록함으로써 DefaultConfigurerAdapter이 비활성화됩니다. @EnableWebSecurity는 WebSecurityEnablerConfiguration에 등록되므로 명시하지않아도 되지만 저는 WebSecurityConfig가 웹 보안 설정을 활성화한다는 의미로 선언했습니다.

```java
/**
 * 웹 보안 설정
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {}
```

##### AuthenticationManagerBuilder
ProviderManager는 다른 AuthenticationManager들에게 인증 요청에 대한 처리를 위임한다고 하였습니다.

> 당연히 기억나시겠죠?...

AuthenticationManagerBuilder는 ProviderManagerBuilder 구현체이기 때문에 ProviderManager라는 AuthenticationManager를 설정할 수 있습니다.

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    User.UserBuilder userBuilder = User.builder();
    String password = "{bcrypt}$2a$12$ry/T4SyQyiNpaWbadf9sne3Cko..q92Oh2klkCMv4XB1qG6cy8iaG";
    
    auth.inMemoryAuthentication().passwordEncoder(passwordEncoder).withUser(userBuilder.username("system").password(password).roles("SYSTEM"));
    auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder).withDefaultSchema().withUser(userBuilder.username("admin").password(password).roles("ADMIN"));
    auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    auth.authenticationProvider(daoAuthenticationProvider);
}
```

##### WebSecurity
WebSecurity 설정 함수를 오버라이딩하면 웹 보안과 관련한 설정을 할 수 있습니다.

다음은 예제에서 사용한 설정입니다.
```java
@Override
public void configure(WebSecurity web) {
    web.ignoring().antMatchers(staticLocations)
            .and()
        .debug(environment.acceptsProfiles(Profiles.of("debug")));
}
```

각 함수는 체이닝 패턴으로 연결할 수 있으며 정적 리소스에 대해서는 웹 보안 설정을 무시하도록 하였고 애플리케이션 구동시 debug 프로필을 포함하는 경우 웹 보안에 대한 디버그 모드를 활성화하도록 설정하였습니다.

> @EnableWebSecurity에서 debug 속성으로 활성화 할 수 있으나 소스코드를 수정해야하는 번거러움이 있습니다.

##### HttpSecurity
HttpSecurity는 WebSecurity와 달리 HTTP 통신과 관련된 보안 사항을 설정할 수 있도록 지원합니다. 

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.formLogin()
            .and()
        .httpBasic();

    http.authorizeRequests()
            .antMatchers("/api/login", "/api/logout").permitAll()
            .antMatchers("/console/**").hasAnyRole("SYSTEM")
            .antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
            .anyRequest().permitAll();

    http.csrf().disable();
}
```

HTTP를 통한 사용자 인증 시 Basic 인증과 폼 양식 기반 인증을 활성화하였습니다. 추가적으로 API 자원에 대해서는 인증된 사용자만 접근 가능하도록하였고 나머지에 대한 요청은 허용한다는 의미입니다.  

이처럼 

#### SecurityConfig.GlobalMethodSecurityConfig
GlobalMethodSecurityConfiguration 클래스를 확장하여 설정을 커스터마이징 할 수 있으며 @EnableGlobalMethodSecurity으로 전역 메소드 보안을 활성화 할 수 있습니다.

```java
/**
 * 메소드 기반 보안 설정
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = false)
@Configuration(proxyBeanMethods = false)
public static class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {}
```

> 사실 GlobalMethodSecurityConfiguration를 확장하는 것은 옵션일 뿐이며, @EnableGlobalMethodSecurity를 WebSecurityConfig에 선언해도 상관없습니다.

### 사용자 정의 기능 구현

#### 개별 사용자 인증 처리하기
다음과 같이 AuthenticationProvider와 SecurityContextHolder를 활용하면 사용자에게 받은 정보로 직접 사용자 인증을 처리할 수 있습니다. 

```java
@Service
public class AuthenticationService extends AbstractService {
    private final AuthenticationProvider authenticationProvider;

    public AuthenticationService(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public UserDetails authentication(User user) {
        return authentication(user.getId(), user.getPassword());
    }

    public UserDetails authentication(String id, String password) {
        LOG.debug("Call authentication({}, {})", id, password);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);
        Authentication authentication = authenticationProvider.authenticate(authenticationToken);
        securityContext.setAuthentication(authentication);
        return (UserDetails) authentication.getPrincipal();
    }
}
```

#### 인증된 사용자 정보 가져오기
인증된 사용자를 가져오는 방법은 여러가지 입니다.

- @AuthenticationPrincipal : AuthenticationPrincipalArgumentResolver가 선언된 매개변수에 인증된 사용자 정보를 넣어줍니다.
- SecurityContextHolder.getContext().getAuthentication().getPrincipal()

> SecurityContextHolder으로부터 인증된 사용자를 가져온다면 NullPointerException 방지 코드를 작성하시기 바랍니다.

```java
@GetMapping("/users/me")
public ResponseEntity<Object> currentUser(@AuthenticationPrincipal UserDetails principal) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(authentication != null) {
        principal = (UserDetails) authentication.getPrincipal();
    }

    return ResponseEntity.ok(principal);
}
```

#### 함수 결과 오브젝트와 인증된 사용자 정보를 비교하기
메소드 기반 접근 제어를 활성화 한 경우 함수로 제공된 매개변수 또는 리턴 오브젝트와 인증된 사용자 정보를 비교할 수 있습니다.

> 자세한 내용은 공식 레퍼런스의 [Method Security Expressions](https://docs.spring.io/spring-security/site/docs/5.2.2.RELEASE/reference/html5/#method-security-expressions)를 참고하세요

만약, 책 소유자만 update 함수 접근을 허용한다고 하면 SpEL 표현식으로 다음과 같이 인증 정보에 포함된 사용자 정보와 비교할 수 있습니다.

```java
@PreAuthorize("#book.authorId == authentication.principal.id")
public boolean update(@P("book") Book book) {
    // 수정 로직
    return true;
}
```

## 끝마치며
