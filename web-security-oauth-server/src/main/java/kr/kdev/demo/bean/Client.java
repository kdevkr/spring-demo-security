package kr.kdev.demo.bean;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Client implements ClientDetails {

    private String name;

    private String clientId;
    private String clientSecret;
    private String authorities;
    private String scope;
    private String authorizedGrantTypes;
    private String resourceIds;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String autoApprove;
    private String additionalInformation;
    private String registeredRedirectUri;

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        if(this.resourceIds == null) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(resourceIds.split(",")));
    }

    @Override
    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    @Override
    public String getClientSecret() {
        return this.clientSecret;
    }

    @Override
    public boolean isScoped() {
        return this.scope != null;
    }

    @Override
    public Set<String> getScope() {
        Set<String> scopes = new HashSet<>();
        if(this.scope == null) {
            // prevent null
            scopes.add("profile");
        } else {
            scopes =  Arrays.stream(scope.split(",")).map(String::trim).collect(Collectors.toSet());
        }
        return scopes;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        if(this.authorizedGrantTypes == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(authorizedGrantTypes.split(",")).map(String::trim).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        if(registeredRedirectUri == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(registeredRedirectUri.split(",")).map(String::trim).collect(Collectors.toSet());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if(this.authorities == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(this.authorities.split(",")).map(authority -> (GrantedAuthority) authority::trim).collect(Collectors.toSet());
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.accessTokenValidity;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.refreshTokenValidity;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
