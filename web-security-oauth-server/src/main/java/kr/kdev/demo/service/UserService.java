package kr.kdev.demo.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.kdev.demo.bean.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private List<User> users;

    public UserService() throws IOException {
        loadUsers();
    }

    private void loadUsers() throws IOException {
        Gson gson = new Gson();
        ClassPathResource classPathResource = new ClassPathResource("db/user.json");
        String clientsJson = StreamUtils.copyToString(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        this.users = gson.fromJson(clientsJson, new TypeToken<List<User>>(){}.getType());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(users != null) {
            for(User user : users) {
                if(user.getUsername().equals(username)) {
                    return user;
                }
            }
        }


        return null;
    }
}
