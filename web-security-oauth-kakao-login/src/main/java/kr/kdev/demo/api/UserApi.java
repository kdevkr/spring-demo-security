package kr.kdev.demo.api;

import kr.kdev.demo.bean.KakaoOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserApi extends BaseApi {
    private final OAuth2AuthorizedClientService authorizedClientService;

    public UserApi(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/users/me")
    public ResponseEntity<Object> currentUser(Authentication authentication, @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(principal);
    }

    @GetMapping("/users/me/token")
    public ResponseEntity<Object> currentUserToken(@AuthenticationPrincipal Principal principal) {
        if(principal instanceof OAuth2AuthenticationToken) {
            Map<String, Object> attributes = new HashMap<>();
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;

            OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), oAuth2AuthenticationToken.getName());
            OAuth2AccessToken accessToken = oAuth2AuthorizedClient.getAccessToken();
            OAuth2RefreshToken refreshToken = oAuth2AuthorizedClient.getRefreshToken();

            attributes.put("name", oAuth2AuthenticationToken.getName());
            attributes.put("accessToken", accessToken);
            attributes.put("refreshToken", refreshToken);
            return ResponseEntity.ok(attributes);
        }

        return ResponseEntity.ok(principal);
    }
}
