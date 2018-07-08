package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.config.ApiVersion;
import kr.co.eceris.projectk.config.SecurityContext;
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

    @ApiVersion(1)
    @GetMapping("/book/search")
    public ResponseEntity<DocumentsVo> search(@RequestParam String query, @RequestParam String sort, @RequestParam String page, @RequestParam String size, @RequestParam String target, @RequestParam String category) {
        Long userId = securityContext.getAuthenticationUserId();
        DocumentsVo search = bookService.search(userId, query, sort, page, size, target, category);
        return ResponseEntity.ok(search);
    }

    @ApiVersion(1)
    @GetMapping("/book/{isbn}")
    public ResponseEntity<DocumentsVo> get(@PathVariable String isbn) {
        DocumentsVo documentsVo = bookApiConnector.search(isbn, null, "1", "1", "isbn", null);
        return ResponseEntity.ok(documentsVo);
    }

    @ApiVersion(1)
    @GetMapping("/book/recent")
    public ResponseEntity<List<BookSearchHistory>> histories() {
        Long userId = securityContext.getAuthenticationUserId();
        List<BookSearchHistory> bookSearchHistories = bookService.searchTop10Histories(userId);
        return ResponseEntity.ok(bookSearchHistories);
    }

    @ApiVersion(1)
    @GetMapping("/bookmarks")
    public ResponseEntity<List<Bookmark>> list() {
        return ResponseEntity.ok(bookService.getBookmarks());
    }

    @ApiVersion(1)
    @PostMapping("/bookmark/{ispn}/title/{title}")
    public ResponseEntity<Bookmark> mark(@PathVariable String isbn, @PathVariable String title) {
        Long userId = securityContext.getAuthenticationUserId();
        return ResponseEntity.ok(bookService.createBookmark(userId, isbn, title));
    }

    @ApiVersion(1)
    @DeleteMapping("/bookmark/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        bookService.deleteBookmark(id);
        return ResponseEntity.ok().build();
    }
}
