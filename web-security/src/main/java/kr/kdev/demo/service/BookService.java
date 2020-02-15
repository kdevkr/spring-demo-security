package kr.kdev.demo.service;

import kr.kdev.demo.bean.Book;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
public class BookService extends AbstractService {

    @PreAuthorize("hasAuthority('BOOK_WRITEABLE')")
    public String create(Book book) {
        // TODO : Implementation
        return null;
    }

    /**
     * {@link Book}의 저자와 인증 {@link java.security.Principal}에 포함된 사용자 아이디를 비교하여
     * 책의 정보를 수정할 수 있는 소유자인지 확인하여 접근 제어를 수행합니다.
     */
    @PreAuthorize("#book.authorId == authentication.principal.id")
    public boolean update(@P("book") Book book) {
        // TODO : Implementation
        return true;
    }
}
