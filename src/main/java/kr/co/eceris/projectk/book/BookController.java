package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookApiConnector bookApiConnector;

    @Autowired
    private BookService bookService;

    @Autowired
    private SecurityContext securityContext;

    @GetMapping("/book/search")
    public ResponseEntity<DocumentsVo> search(@RequestParam String query, @RequestParam String sort, @RequestParam String page, @RequestParam String size, @RequestParam String target, @RequestParam String category) {
        Long userId = securityContext.getAuthenticationUserId();
        DocumentsVo search = bookService.search(userId, query, sort, page, size, target, category);
        return ResponseEntity.ok(search);
    }

    @GetMapping("/book/{isbn}")
    public ResponseEntity<DocumentsVo> get(@PathVariable String isbn) {
        DocumentsVo documentsVo = bookApiConnector.search(isbn, null, "1", "10", "isbn", null);
        return ResponseEntity.ok(documentsVo);
    }

    @GetMapping("/book/recent")
    public ResponseEntity<List<BookSearchHistory>> histories() {
        Long userId = securityContext.getAuthenticationUserId();
        List<BookSearchHistory> bookSearchHistories = bookService.searchTop10Histories(userId);
        return ResponseEntity.ok(bookSearchHistories);
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<List<Bookmark>> list() {
        return ResponseEntity.ok(bookService.list());
    }

    @PostMapping("/bookmark/{ispn}/title/{title}")
    public ResponseEntity<Bookmark> mark(@PathVariable String isbn, @PathVariable String title) {
        return ResponseEntity.ok(bookService.create(isbn, title));
    }

    @DeleteMapping("/bookmark/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }
}
