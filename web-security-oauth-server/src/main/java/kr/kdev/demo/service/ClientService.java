package kr.kdev.demo.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.kdev.demo.bean.Client;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ClientService implements ClientDetailsService {

    private List<Client> clients;

    public ClientService() throws IOException {
        loadClients();
    }

    private void loadClients() throws IOException {
        Gson gson = new Gson();
        ClassPathResource classPathResource = new ClassPathResource("db/client.json");
        String clientsJson = StreamUtils.copyToString(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        this.clients = gson.fromJson(clientsJson, new TypeToken<List<Client>>(){}.getType());
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        if(clients != null) {
            for (Client client : clients) {
                if (client.getClientId().equals(clientId)) {
                    return client;
                }
            }
        }

        return null;
    }
}
