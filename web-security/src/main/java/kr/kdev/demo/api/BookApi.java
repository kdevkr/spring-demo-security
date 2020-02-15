package kr.kdev.demo.api;

import kr.kdev.demo.bean.Book;
import kr.kdev.demo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookApi extends BaseApi {

    private final BookService bookService;

    public BookApi(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/books/{bookId}")
    public ResponseEntity<Object> update(@PathVariable String bookId, @ModelAttribute Book book) {
        book.setBookId(bookId);
        return ResponseEntity.ok(bookService.update(book));
    }
}
