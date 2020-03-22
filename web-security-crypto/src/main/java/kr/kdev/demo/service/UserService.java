package kr.kdev.demo.service;

import kr.kdev.demo.bean.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 인증 서비스
 * @author kdevkr
 */
@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // TODO : Implementation
        User user = new User();
        user.setId(username);
        user.setPassword("{bcrypt}$2a$12$ry/T4SyQyiNpaWbadf9sne3Cko..q92Oh2klkCMv4XB1qG6cy8iaG");

        return user;
    }
}
