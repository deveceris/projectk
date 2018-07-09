package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.auth.WebSecurityContext;
import kr.co.eceris.projectk.config.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {

    @Autowired
    private BookApiConnector bookApiConnector;

    @Autowired
    private BookService bookService;

    @Autowired
    private WebSecurityContext webSecurityContextt;


    @ApiVersion(1)
    @GetMapping("/api/book/search")
    public ResponseEntity<DocumentsVo> search(@RequestParam String query, @RequestParam(required = false) String sort, @RequestParam(required = false) String page, @RequestParam(required = false) String size, @RequestParam(required = false) String target, @RequestParam(required = false) String category) {
//        Long userId = webSecurityContextt.getAuthenticationUserId();
        Long userId = 1l;
        DocumentsVo search = bookService.search(userId, query, sort, page, size, target, category);
        return ResponseEntity.ok(search);
    }

    @ApiVersion(1)
    @GetMapping("/api/book/{isbn}")
    public ResponseEntity<DocumentsVo> get(@PathVariable String isbn) {
        Long authenticationUserId = webSecurityContextt.getAuthenticationUserId();
        DocumentsVo documentsVo = bookApiConnector.search(isbn, null, "1", "1", "isbn", null);
        return ResponseEntity.ok(documentsVo);
    }

    @ApiVersion(1)
    @GetMapping("/api/book/recent")
    public ResponseEntity<List<BookSearchHistory>> histories() {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        List<BookSearchHistory> bookSearchHistories = bookService.searchTop10Histories(userId);
        return ResponseEntity.ok(bookSearchHistories);
    }

    @ApiVersion(1)
    @GetMapping("/api/bookmarks")
    public ResponseEntity<List<BookmarkVo>> list() {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        List<Bookmark> bookmarks = bookService.getBookmarks(userId);
        return ResponseEntity.ok(bookmarks.stream().map(bookmark -> bookmark.toVo()).collect(Collectors.toList()));
    }

    @ApiVersion(1)
    @PostMapping("/api/bookmark/{isbn}/title/{title}")
    public ResponseEntity<BookmarkVo> mark(@PathVariable String isbn, @PathVariable String title) {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        return ResponseEntity.ok(bookService.createBookmark(userId, isbn, title).toVo());
    }

    @ApiVersion(1)
    @DeleteMapping("/api/bookmark/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        bookService.deleteBookmark(id);
        return ResponseEntity.ok().build();
    }
}
