package kr.kdev.demo.api;

import kr.kdev.demo.bean.CurrentUser;
import kr.kdev.demo.bean.Repository;
import kr.kdev.demo.bean.User;
import kr.kdev.demo.service.RepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.bind.annotation.*;

@RestController
public class RepositoryApi extends BaseApi {

    private final RepositoryService repositoryService;

    public RepositoryApi(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @PostMapping("/repository")
    public ResponseEntity<Object> create(@ModelAttribute Repository repository, @CurrentUser User user) {
        TextEncryptor textEncryptor = Encryptors.noOpText();
        String userId = user.getUserId();

        repository.setId(textEncryptor.encrypt(repository.getName() + "/" + userId));
        repository.setOwnerId(userId);

        String newRepositoryId = repositoryService.create(repository);
        return ResponseEntity.ok(newRepositoryId);
    }

    @PutMapping("/repository/{repositoryId}")
    public ResponseEntity<Object> update(@PathVariable String repositoryId,
                                         @ModelAttribute Repository repository) {
        repository.setId(repositoryId);
        return ResponseEntity.ok(repositoryService.update(repository));
    }

    @DeleteMapping("/repository/{repositoryId}")
    public ResponseEntity<Object> delete(@PathVariable String repositoryId) {
        return ResponseEntity.ok(null);
    }
}
