> 2017.03.13 ~ 2017.03.28

## Spring Security Study History    

### 1. Spring-Demo-Security  
- Spring Tool Suite (Eclipse)  
- Java Configuration  
- Gradle  
- Basic Tutorial with Spring Security Reference


- `springSecurityFilterChain`  
- `@EnableWebSecurity`  
- `WebSecurityConfigurerAdapter`  
- `inMemoryAuthentication`  

### 2. Spring-Demo-Security-Advanced  
- based Spring-Demo-Security  
- Advanced Tutorial with Spring Security Reference  


- `Localization`  
- `CSRF Cookie Token`  
- `Token Based Remember-Me with inMemoryAuthentication`  
- `SessionManagerment`  

### 3. Spring-Demo-Security-JPA  
- based Spring-Demo-Security  
- Basic Tutorial with Spring Data JPA


- `Localization`  
- `UserDetails`  
- `UserDetailsService`  

### 4. Spring-Demo-Security-JPA-Advanced  
- based Spring-Demo-Security-JPA  
- Advanced Tutorial with Spring Data JPA


- `Persistent Based Remember-Me with PersistentAuthentication`  
- `SessionManagerment`  
- `AuthenticationSuccessHanler`  
- `AuthenticationFailureHanler`  
- `PasswordEncoder`

---
## 1. 스프링 시큐리티란 무엇인가?  
스프링 시큐리티를 이해하기 위해서 스프링 시큐리티가 무엇인지를 알아야합니다. 스프링 시큐리티 레퍼런스에서는 자바 EE 기반의 엔터프라이즈 소프트웨어 애플리케이션을 위한 포괄적인 보안 서비스들을 제공하고 오픈 플랫폼이면서 자신만의 인증 매커니즘을 간단하게 만들 수 있다고 자랑(?)하고 있습니다.

하지만, 신입 개발자들 수준에서 스프링 시큐리티와 같은 보안 기술을 이해하기란 참 힘든 과정이라고 생각합니다.

> 저만 이해하기 힘들 수 있어요 ㅠㅠ  

스프링 시큐리티를 이해하기 위해서는 스프링 시큐리티가 애플리케이션 보안을 구성하는 두가지 영역에 대해서 알아야 합니다. 바로 인증(Authentication)과 권한(Authorization)이라는 것입니다. 이 두 영역은 스프링 시큐리티의 목표이기 때문에 반드시 이해하고 넘어가야 합니다. `인증`은 애플리케이션의 작업을 수행할 수 있는 주체(사용자)라고 주장할 수 있는 것을 말하며 권한은 인증된 주체가 애플리케이션의 동작을 수행할 수 있도록 허락되있는지를 결정하는 것을 말합니다. 따라서, 권한 승인이 필요한 부분으로 접근하려면 인증 과정을 통해 주체가 증명 되어야만 한다는 것입니다.   

앞서 스프링 시큐리티는 자신만의 인증 매커니즘을 간단하게 만들 수 있다고 자랑한다고 했는데요. 스프링 시큐리티는 이와 관련해서 인증 매커니즘과 관계없이 상당히 깊은 권한 부여를 제공하고 있습니다. 권한 부여에도 두가지 영역이 존재하는데 웹 요청 권한, 메소드 호출 및 도메인 인스턴스에 대한 접근 권한 부여입니다. 스프링 시큐리티는 이렇게 모든 중요한 영역에 필요한 기능을 제공하고 있습니다.

[제타건담님의 글](http://zgundam.tistory.com/43)에서는 로그인 화면을 통해서 아이디와 비밀번호를 입력받아 로그인하는 과정을 인증이라고 서술하고 있습니다. 이와 관련된 용어가 바로 HTTP 기본 인증(HTTP Basic Authentication) 매커니즘이라고 할 수 있습니다. 그리고 이 HTTP 기본 인증 매커니즘을 이용하는 방식이 바로 스프링 시큐리티 레퍼런스에서 설명하는 폼 기반 로그인이 됩니다.  

> 아 물론, HTTP 기본 인증 == 폼 기반 로그인이라는 말은 아닙니다. 폼 양식을 통한 로그인도 username과 password를 통해 인증하기 때문에 HTTP 기본 인증 매커니즘을 이용하는 방식이라고 이해할 수 있다는 것입니다.  

실제로 [스프링 시큐리티 레퍼런스 문서의 Basic and Digest Authentication](http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#basic)에서는 다음을 언급하고 있습니다.  

Basic authentication is often used with stateless clients which pass their credentials on each request. It’s quite common to use it in combination with form-based authentication where an application is used through both a browser-based user interface and as a web-service.

> 인증과 권한이라는 개념을 이해하셨다면 스프링 시큐리티에 대해서 배워보도록 하겠습니다.

## 2. 스프링 시큐리티 시작하기  
스프링 시큐리티는 메이븐이나 그래들같은 빌드도구를 통해 쉽게 가져올 수 있습니다.  

#### with Maven  
```xml
<dependencies>
<!-- ... other dependency elements ... -->
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-web</artifactId>
	<version>4.2.2.RELEASE</version>
</dependency>
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-config</artifactId>
	<version>4.2.2.RELEASE</version>
</dependency>
</dependencies>
```

#### with Gradle  
```javascript
dependencies {
	compile 'org.springframework.security:spring-security-web:4.2.2.RELEASE'
	compile 'org.springframework.security:spring-security-config:4.2.2.RELEASE'
}
```

> 저는 메이븐 대신 그래들을 사용해봤습니다.

### 2.1 Java Configuration   
스프링 시큐리티 레퍼런스에서는 자바 기반의 설정으로 설명하고 있습니다. 그 이유는 무엇일까요?  

스프링 프레임워크 3.1에서부터 어노테이션을 통한 자바 설정을 지원하기 때문에 스프링 시큐리티 3.2부터는 XML로 설정하지 않고도 간단하게 설정할 수 있도록 지원하기 때문입니다.  

원래 XML 기반의 설정에서는 web.xml에 org.springframework.web.filter.DelegatingFilterProxy라는 springSecurityFilterChain을 등록하는 것으로 시작합니다만, 자바 기반의 설정에서는 WebSecurityConfigurerAdapter를 상속받은 클래스에 @EnableWebSecurity 어노테이션을 명시하는 것만으로도 springSecurityFilterChain가 자동으로 포함되어집니다.    

```java
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
}
```

그리고 포함된 springSecurityFilterChain을 등록하기 위해서는 AbstractSecurityWebApplicationInitializer를 상속받은 WebApplicationInitializer를 만들어두면 됩니다.  

```java
public class SecurityWebApplicationInitializer
	extends AbstractSecurityWebApplicationInitializer {

}
```

> XML을 작성하는 것보다는 자바 기반의 구성이 더욱 쉽습니다.  

그 다음에는 보통 스프링 MVC를 이용해서 애플리케이션을 구성하기 때문에 ApplicationIniticalizer에 WebSecurityConfigurerAdapter를 상속받은 클래스를 getRootConfigClasses() 메소드에 추가하는 것으로 스프링 시큐리티에 대한 기본적인 적용은 끝입니다.  

```java
public class ApplicationInitializer extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { WebSecurityConfig.class };
	}

	// ... other overrides ...
}
```

>이로써 유추 해볼때, springSecurityFilterChain은 루트 컨텍스트에서 등록되어야하는 것을 알 수 있겠습니다. 각 필터에 대한 우선순위에 대해서도 추가적으로 알 필요성이 생기는 부분입니다.    

### 2.2 HttpSecurity  
그리고나서 configure(HttpSecurity http) 메소드를 통해서 우리만의 인증 매커니즘을 구성해야합니다. 그런데 아직까지는 어떻게 구성해야할지 막막하기만 합니다. 관련된 정보를 좀더 찾아보도록 하겠습니다.  

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
	http.httpBasic()
		.and()
		.authorizeRequests()
			.antMatchers("/users/{userId}").access("@authenticationCheckHandler.checkUserId(authentication,#userId)")
			.antMatchers("/admin/db/**").access("hasRole('ADMIN_MASTER') or hasRole('ADMIN') and hasRole('DBA')")
			.antMatchers("/register/**").hasRole("ANONYMOUS")
		.and()
		.formLogin()
			.loginPage("/login")
			.usernameParameter("email")
			.passwordParameter("password")
			.successHandler(successHandler())
			.failureHandler(failureHandler())
			.permitAll();
}
```

뭐 사실상 별게 없습니다. HttpSecurity의 인스턴스를 통해 자신만의 인증 매커니즘을 만들 수 있는 것이라고 보면 됩니다. 대부분의 설정은 여기를 통하게 되니까요 .authorizeRequests()를 통해 요청에 대한 권한을 지정할 수 있게 되는 것 같습니다. 그리고 기본적인 형태의 `.anyRequest().authenticated()`라는 의미는 어떠한 요청이든지 인증되어야한다는 것이겟죠? .formLogin()은 폼을 통한 로그인을 이용한다는 의미이며, .loginPage("/login")을 통해 로그인 페이지는 /login 경로로 제공하며 /login의 POST 요청이 바로 로그인 처리 과정이라는 것입니다.  

확장된 형태에서의 .antMatchers().hasRole() 또는 .antMatchers().access()는 해당 경로에 대해서 어떠한 권한을 가져야만 접근할 수 있는가를 표현하는 것입니다. 다음은 antMatchers() 다음으로 지정할 수 있는 항목들입니다.  

- anonymous()  
인증되지 않은 사용자가 접근할 수 있습니다.  
- authenticated()  
인증된 사용자만 접근할 수 있습니다.  
- fullyAuthenticated()  
완전히 인증된 사용자만 접근할 수 있습니다(?)  
- hasRole() or hasAnyRole()  
특정 권한을 가지는 사용자만 접근할 수 있습니다.  
- hasAuthority() or hasAnyAuthority()  
특정 권한을 가지는 사용자만 접근할 수 있습니다.  
- hasIpAddress()  
특정 아이피 주소를 가지는 사용자만 접근할 수 있습니다.  
- access()  
SpEL 표현식에 의한 결과에 따라 접근할 수 있습니다.  
- not()
접근 제한 기능을 해제합니다.   
- permitAll() or denyAll()  
접근을 전부 허용하거나 제한합니다.  
- rememberMe()  
리멤버 기능을 통해 로그인한 사용자만 접근할 수 있습니다.  

> Role은 역할이고 Authority는 권한이지만 사실은 표현의 차이입니다. Role은 "ADMIN"으로 표현하고 Authority는 "ROLE_ADMIN"으로 표기하는 것 뿐입니다. 실제로 hasRole()에 ROLE_ADMIN으로 표기하면 ROLE을 지우라는 에러를 볼수 있게 됩니다.  

> 스프링 시큐리티 태그 라이브러리 또한 SpEL 표현식을 사용할 수 있습니다. \<sec:authentication \/\> 이렇게 말입니다.  

### 2.3 AuthenticationManagerBuilder  
AuthenticationManagerBuilder를 통해 인증 객체를 만들 수 있도록 제공하고 있습니다. 아래는 스프링 시큐리티 레퍼런스에서 알려주는 방식인데 AuthenticationManagerBuilder를 메소드를 통해 주입받아 처리하는 방식입니다. WebSecurityConfigurerAdapter의 configure(AuthenticationManagerBuilder auth)를 오버라이딩하는 것에 대한 차이는 없습니다.

```java
@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	auth.inMemoryAuthentication().withUser("scott").password("tiger").roles("ROLE_USER");
}

@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	// TODO Auto-generated method stub
	auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN", "DBA");
	auth.inMemoryAuthentication().withUser("scott").password("tiger").roles("USER", "SETTING");
}
```

원하는 방식을 사용하면 됩니다. 하지만, 스프링 시큐리티 레퍼런스와 샘플을 만든 개발자가 왜 첫번째 방식으로 설명하는지에 대한 의미를 생각해봅시다.  

> 아시는 분이 있다면 알려주시면 감사하겠습니다.  

## 3. 스프링 시큐리티 속으로 들어가기
지금까지 했던 설정들은 아주 기본적인 형태들이기 때문에 실제로 우리만의 인증 매커니즘으로 커스터마이징을 한 뒤 스프링 시큐리티에서 제공하는 부가적인 기능을 추가해야합니다.  

### 3.1 Method Security  
스프링 시큐리티 2.0 에서부터 서비스 계층의 메소드에 보안을 추가할 수 있도록 지원합니다. @Secured 어노테이션 뿐만 아니라 JSR-250 어노테이션도 지원하도록 제공됩니다. 스프링 시큐리티 3.0에서는 표현 기반의 어노테이션을 사용할 수 있게 됩니다.  

우리는 Configuration 클래스에 @EnableGlobalMethodSecurity를 적용함으로써 어노테이션 기반의 보안을 활성화시킬 수 있습니다. 예를 들어, 스프링 시큐리티의 @Secured 어노테이션을 활성화시키려면 다음과 같이 구성하게 됩니다.  

```java
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
// ...
}
```

그리고나서 클래스 또는 인터페이스의 메소드에 @Secured 어노테이션을 추가하면 그에 따른 해당 메소드에 대한 액세스가 제한됩니다.  

```java
public interface BankService {

@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
public Account readAccount(Long id);

@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
public Account[] findAccounts();

@Secured("ROLE_TELLER")
public Account post(Account account, double amount);
}
```

> 역할 단위로 제약조건을 지정할 수 있음을 알 수 있습니다.

스프링 시큐리티 3.0부터 지원하는 표현식 기반의 문법을 사용하기 위해서는 다음과 같이 구성합니다.
```java
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {
// ...
}
```

```java
public interface BankService {

@PreAuthorize("isAnonymous()")
public Account readAccount(Long id);

@PreAuthorize("isAnonymous()")
public Account[] findAccounts();

@PreAuthorize("hasAuthority('ROLE_TELLER')")
public Account post(Account account, double amount);
}
```

### 3.2 Remember-Me  
리멤버-미 기능은 도대체 무엇일까요? 구글에 what is remember me 라는 키워드로 검색을 해보았습니다.

- [What-does-Remember-Me-mean-on-the-Login-page](https://www.formsite.com/faq/general/What-does-Remember-Me-mean-on-the-Login-page.html)  
- [What is "Remember Me" ?](http://help.thunderbird.edu/portal-login/remember-me)  

단순히 아이디를 기억해놓는 것이 아니라 로그인 정보를 유지하는 것을 말합니다.  

Do NOT use "Remember Me" on any public computer, on campus, in Internet cafe's, or anywhere else where you cannot control access!

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
	http.rememberMe().rememberMeParameter("remember-me").key(REMEMBER_ME_KEY).rememberMeServices(persistentTokenBasedRememberMeServices());
}

@Bean
public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices(){
	PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices =
			new PersistentTokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService, persistentTokenRepository());
	return persistentTokenBasedRememberMeServices;
}

@Bean
public PersistentTokenRepository persistentTokenRepository(){
	TokenRepositoryImpl tokenRepositoryImpl = new TokenRepositoryImpl();
	return tokenRepositoryImpl;
}
```

저는 영속성 기반의 리멤버-미 기능을 추가하였습니다. 이때, 리멤버-미 토큰을 저장할 수 있도록 TokenRepository 인터페이스를 구현해야하는데요. 일종의 서비스 객체라고 생각하시면 됩니다.  

```java
@Transactional
public class TokenRepositoryImpl  implements PersistentTokenRepository {

	@Autowired
	private TokenRepository tokenRepository;

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		// TODO Auto-generated method stub
		Token newToken = new Token();
		newToken.setEmail(token.getUsername());
		newToken.setToken(token.getTokenValue());
		newToken.setLast_used(token.getDate());
		newToken.setSeries(token.getSeries());
		tokenRepository.save(newToken);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		// TODO Auto-generated method stub

		Token updateToken = tokenRepository.findOne(series);
		updateToken.setToken(tokenValue);
		updateToken.setLast_used(lastUsed);
		updateToken.setSeries(series);
		tokenRepository.save(updateToken);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String series) {
		// TODO Auto-generated method stub
		Token token = tokenRepository.findOne(series);
		PersistentRememberMeToken persistentRememberMeToken = new PersistentRememberMeToken(token.getEmail(), series, token.getToken(), token.getLast_used());
		return persistentRememberMeToken;
	}

	@Override
	public void removeUserTokens(String username) {
		// TODO Auto-generated method stub
		tokenRepository.deleteByEmail(username);
	}
}
```

`본 애플리케이션에서의 Email은 유니크한 속성을 가집니다.`

### 3.3 Password Encoding  
AuthenticationManagerBuilder.userDetailsService().passwordEncoder()를 통해 패스워드 암호화에 사용될 PasswordEncoder 구현체를 지정할 수 있습니다. PasswordEncoder 인터페이스는 다음과 같이 구성되어 있습니다.  

```java
public interface PasswordEncoder {

	/**
	 * Encode the raw password. Generally, a good encoding algorithm applies a SHA-1 or
	 * greater hash combined with an 8-byte or greater randomly generated salt.
	 */
	String encode(CharSequence rawPassword);

	/**
	 * Verify the encoded password obtained from storage matches the submitted raw
	 * password after it too is encoded. Returns true if the passwords match, false if
	 * they do not. The stored password itself is never decoded.
	 *
	 * @param rawPassword the raw password to encode and match
	 * @param encodedPassword the encoded password from storage to compare with
	 * @return true if the raw password, after encoding, matches the encoded password from
	 * storage
	 */
	boolean matches(CharSequence rawPassword, String encodedPassword);
}
```
> 단순히 인코딩하는 함수와 평문으로 제공되는 패스워드와 인코딩되어있는 패스워드(예를들어, 데이터베이스에 인코딩되어 저장되어 있는 패스워드)를 비교할 수 있습니다.  

저는 PasswordEncoder 구현체인 BCryptPasswordEncoder를 지정했습니다. 당연히 직접 구현체를 만들어서 적용해도 됩니다! (아무래도 직접 만들어서 사용하는 것이 더 좋겟죠?)  

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	// TODO Auto-generated method stub
	auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
}

@Bean
public PasswordEncoder passwordEncoder(){
	return new BCryptPasswordEncoder();
}
```

PasswordEncoder를 빈으로 등록해두면 다음과 같이 저장된 패스워드를 비교할 수 있습니다. 저장된 패스워드는 PasswordEncoder에 의해 암호화된 평문이기 때문입니다.

```java
@RequestMapping(value="/users/{userId}", method=RequestMethod.POST)
@PreAuthorize("#updateUser.email == authentication.name")
public String update(@PathVariable("userId") Long id, @ModelAttribute @Valid UserDTO.Update updateUser, Model model){
	User currentUser = userService.findOne(id);
	if(!passwordEncoder.matches(updateUser.getPassword(), currentUser.getPassword())){
		throw new RuntimeException("Not password equals...");
	}
	//...
}
```

### 3.4 WebSecurity Ignoring  
스프링 시큐리티 레퍼런스와는 달리 리소스와 관련해서는 `WebSecurity.ignoring()`를 이용해서 보안이 적용되지 않도록 할 수 있도록 지원합니다. 스프링 시큐리티 API 문서에서 확인할 수 있는데 레퍼런스에 설명이 없다는 것이 좀 아쉬운 부분입니다.  

`음... permitAll과 ignoring의 차이가 있는가를 아시는분은 댓글 남겨주시기 바랍니다 ㅠㅠ`  

### 3.5 Localization  
스프링 시큐리티는 메시지에 대한 현지화를 지원합니다. 메시지 소스 관련 프로퍼티 파일들은 spring-security-core.jar에 포함 되어져있습니다.  

우리는 메시지 프로퍼티 파일들을 메시지소스로 등록하면 됩니다.  

```java  
@Configuration
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {

	@Bean
	public MessageSource messageSource(){
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.addBasenames("security/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor(){
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Bean
	public LocaleResolver localeResolver(){
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.KOREAN);
		return localeResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(localeChangeInterceptor());
	}
}
```

> 메시지 프로퍼티 파일들은 src/main/resource/security 폴더에 위치하고 있습니다.

### 3.6 AuthenticationSuccessHandler & AuthenticationFailureHandler  
OKKY의 질문게시판에서 로그인 실패를 어떻게 체크하는가에 대해서 질문하는 글을 보았습니다. 저는 스프링 시큐리티에서 사용하는 AuthenticationFailureHandler 구현체를 상속받아서 처리하도록 하겠습니다.  

```java
public class AuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler  {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		logger.info("Login Success... - {}", authentication.getPrincipal());
		response.sendRedirect("/?login");
	}

}
```

AuthenticationSuccessHandler 구현체에서는 로그인을 성공했을때 호출(인증 객체가 생성되어진 후)되기 때문에 Authentication 인스턴스 파라미터를 이용할 수 있습니다. authentication.getPrincipal()을 호출하게 되면 저 같은 경우에는 org.springframework.security.core.userdetails.User 가 아닌 com.kdev.app.security.userdetails.UserDetails를 이용할 수 있습니다.  

```java
public class AuthenticationFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFailureHandlerImpl.class);

	public AuthenticationFailureHandlerImpl() {
		this.setDefaultFailureUrl("/login?error");
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		logger.info("Login Failed... - {}",request.getParameter("email"));
		super.onAuthenticationFailure(request, response, exception);
	}

}
```

로그인 실패 시 SimpleUrlAuthenticationFailureHandler의 defaultFailureUrl를 지정하면 SPRING_SECURITY_LAST_EXCEPTION에 대한 정보를 가지면서 해당 경로로 이동하게 됩니다.  

AuthenticationException으로 로그인 실패의 이유도 체크할 수 있게 되죠  

## 4. 스프링 시큐리티 페이지  

스프링 시큐리티에서 제공해주는 기본 로그인 페이지 대신에 우리만의 로그인 페이지를 만들어보도록 하겠습니다.  

```html
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!--
		<meta id="_csrf" name="_csrf" content="${_csrf.token}" />
		<meta id="_csrf_header" name="_csrf_header" content="${_csrf.headerName}" />
	 -->
	<sec:csrfMetaTags />
	<title>Spring Security + JPA</title>

	<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>
<body>
<div class="container">
	<div class="page-header">
	  <h1>Login Page <small>with Bootstrap</small></h1>
	</div>
	<div class="container-fluid">
		<form method="post" action="/login">
		<%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> --%>
		  <sec:csrfInput />
		  <div class="form-group">
		    <label for="email">이메일</label>
		    <input type="text" class="form-control" name="email" placeholder="Email" required>
		  </div>
		  <div class="form-group">
		    <label for="password">비밀번호</label>
		    <input type="password" class="form-control" name="password" placeholder="Password" required>
		  </div>
		  <div class="checkbox">
		    <label>
		      <input type="checkbox" name="remember-me" > Remember me
		    </label>
		  </div>
		  <div class="text-center">
		  	<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
		  		<span class="text-danger"><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/></span>
		  		<c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session"/>
		  	</c:if>
		  </div>
		  <hr>
		  <button type="submit" class="btn btn-default">로그인</button>
		  <a class="btn btn-default" href="/register">회원가입</a>
		</form>
	</div>
	<div class="container">

	</div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!--[if lt IE 9]>
   <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
   <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</body>
</html>
```

`SPRING_SECURITY_LAST_EXCEPTION`의 존재 여부에 따라서 스프링 시큐리티에서 발생하는 예외에 대한 메시지를 출력할 수 있습니다.  

> 스프링 시큐리티 레퍼런스에서는 단순히 파라미터의 존재여부에 따라서 출력형태를 지정하고 있습니다. 이 부분도 참 아쉽습니다.  

## 5. UserDetails & UserDetailsService  
아 죄송합니다. UserDetails 인터페이스에 대해서 설명을 안했네요 ㅠㅠ  

스프링 시큐리티는 사용자 정보를 UserDetails 구현체로 사용합니다. 그래서 스프링 시큐리티는 org.springframework.security.core.userdetails.User라는 클래스를 제공합니다. 그러나, 이름과 패스워드 그리고 권한들에 대한 필드만 존재하기 때문에 이메일 정보 또는 프로필 이미지 경로 등과 같은 부가적인 정보를 담을 수 없습니다.  

따라서, UserDetails 구현체를 직접 만들어야 합니다. org.springframework.security.core.userdetails.User 자체도 UserDetails 구현체이기 때문에 이를 상속받아서 구현해도 됩니다.  

```java
public class UserDetails extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = -4855890427225819382L;

	private Long id;
	private String nickname;
	private String email;
	private Date createdAt;


	public UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		// TODO Auto-generated constructor stub
	}

	public UserDetails(User user){
		super(user.getEmail(), user.getPassword(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.isEnabled(), authorities(user));

		this.id = user.getId();
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.createdAt = user.getCreatedAt();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	private static Collection<? extends GrantedAuthority> authorities(User user) {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		user.getAuthorities().forEach(a -> {
			authorities.add(new SimpleGrantedAuthority(a.getAuthority()));
		});
		return authorities;
	}

	public UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
			String nickname) {
		super(username, password, authorities);
		this.nickname = nickname;
		this.email = username;
	}

	public UserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		// TODO Auto-generated constructor stub
	}
}
```

그러면 우리는 Authentication.getPrincipal() 메소드를 통해 얻은 Principal 객체를 통해서도 부가적인 필드에 접근할 수 있습니다.  

```html
<sec:authentication property="principal.email"/>
```

org.springframework.security.core.userdetails.UserDetailsService 구현체는 스프링 시큐리티 인증 시에 사용됩니다. UserRepository를 통해 영속성으로 저장된 인증정보를 검색한 후 존재하지 않다면 UsernameNotFoundException을 던지고 있다면 UserDetails 객체를 반환합니다.  

```java  
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(email);
		if(user == null){
			throw new UsernameNotFoundException(email);
		}
		com.kdev.app.security.userdetails.UserDetails userDetails = new com.kdev.app.security.userdetails.UserDetails(user);
		return userDetails;
	}
}
```

그런데 좀 이상하다고 느껴집니다. 그러면 패스워드 검증은 언제하는것일까요? 바로 AuthenticationProvider 구현체에서 진행하게 됩니다. AuthenticationProvider 구현체에서는 authenticate() 메소드를 통해서 Authentication 객체(UsernamePasswordAuthentication)를 반환합니다. 즉, 반환하기 직전에 패스워드를 검증하는 것입니다.  

우리는 AuthenticationProvider를 직접적으로 구현하지 않았으니까 이 부분에 대해서 모르고 넘어갈 뻔 했습니다.

[샤쿠님의 스프링 시큐리티 커스텀 로그인](http://syaku.tistory.com/286)이라는 글에서 직접 AuthenticationProvider를 구현해서 사용하는 것을 확인할 수 있습니다.

# 끝마치며  
우리가 알아본 것 이외에도 스프링 시큐리티가 제공하는 기능들이 더 존재합니다. 예를 들어, 웹소켓, RESTful API등에도 보안을 적용할 수 있습니다.

공부하면서 알게된 부분인데, 스프링 프레임워크에서 제공하는 인-메모리 기반 인증 객체(inMemoryAuthentication())는 파라미터로 제공되는 UserDetails의 정보를 활용하여 User 객체를 생성하기 때문에 UserDetails의 구현체를 만든다 하더라도 의미가 없게 됩니다. 만약, 인 메모리 방식과 영속성 방식을 혼용해서 사용하고 싶다면 이 부분에 대해서 연구해볼 필요가 있겠습니다.  

다음은 스프링 시큐리티에 대해서 알아볼 때 좋은 포스트 및 동영상들입니다.  

- [샤쿠님의 스프링 시큐리티 커스텀 로그인](http://syaku.tistory.com/278)  
- [제타건담님의 스프링 시큐리티](http://zgundam.tistory.com/43)  
- [아라한사님의 스프링 시큐리티 따라해보기](https://www.youtube.com/watch?v=C0BQplG7Epo&list=PLvudjKUrAA6bLu1CvgSPEKLhlIEIAJXjm)  
- [백기선님의 스프링 시큐리티](https://www.youtube.com/watch?v=AiDjJzMXWmM)  
- [하마님의 스프링 시큐리티](http://hamait.tistory.com/325)  

> 아는게 없으니 쉽게 이해하지 못하는 부분이 많습니다. 많은 신입 개발자들을 응원합니다 ^ㅡ^  

