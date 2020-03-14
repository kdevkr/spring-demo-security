package kr.kdev.demo.service;

import kr.kdev.demo.bean.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService Implementation.
 * @author kdevkr
 */
@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("system") || username.equals("admin")) {
            throw new UsernameNotFoundException("error.notFound");
        }

        // 여러분의 사용자 DB를 기반으로 UserDetails 구현체를 제공할 수 있습니다.
        User user = new User();
        user.setId(username);
        user.setName(username);
        user.setPassword("{bcrypt}$2a$12$ry/T4SyQyiNpaWbadf9sne3Cko..q92Oh2klkCMv4XB1qG6cy8iaG");



        return user;
    }
}
