package kr.kdev.demo.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.postgresql.jdbc.PgArray;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static kr.kdev.demo.config.SecurityConfig.PRINCIPAL_LOCK_BASELINE;

@Data
public class User implements UserDetails {

    private String userId = UUID.randomUUID().toString();
    private String id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String name;
    private String role;
    private List<String> authorities;

    private int loginFailCount;
    private Long expiredAt;
    private Long passwordExpiredAt;
    private boolean deleted;

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        if(this.authorities != null) {
            List<SimpleGrantedAuthority> grantedAuthorities = this.authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            authorities.addAll(grantedAuthorities);
        }

        if(this.role != null) {
            authorities.add((GrantedAuthority) () -> "ROLE_" + this.role);
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return expiredAt == null || (expiredAt < System.currentTimeMillis());
    }

    @Override
    public boolean isAccountNonLocked() {
        return loginFailCount < PRINCIPAL_LOCK_BASELINE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return passwordExpiredAt == null || (passwordExpiredAt < System.currentTimeMillis());
    }

    @Override
    public boolean isEnabled() {
        return !deleted;
    }

    public void setAuthorities(PgArray pgArray) throws SQLException {
        Object array = pgArray.getArray();
        String[] values = (String[]) array;
        List<String> strings = Arrays.asList(values);
        this.authorities = strings;
    }
}
