package kr.kdev.demo.service;

import kr.kdev.demo.bean.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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

    private final JdbcTemplate jdbcTemplate;
    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if("system".equals(username)) {
            throw new UsernameNotFoundException("error.notFound");
        }

        User user = this.getUser(username);
        if(user == null) {
            throw new UsernameNotFoundException("error.notFound");
        }

        return user;
    }

    public User getUser(String id) {
        User user = jdbcTemplate.queryForObject("SELECT u.*, array_remove(array_agg(ua.authority), NULL) AS authorities FROM users u" +
                " LEFT JOIN users_authority ua ON u.user_id = ua.user_id" +
                " WHERE u.id = ?" +
                " GROUP BY u.user_id", new Object[]{id}, new BeanPropertyRowMapper<>(User.class));
        return user;
    }
}
