package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.ApiResponse;
import kr.co.eceris.projectk.config.WebSecurityContext;
import kr.co.eceris.projectk.config.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Optional;
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
    @GetMapping("/book/search")
    public ResponseEntity<ApiResponse> search(@RequestParam String query, @RequestParam(required = false) String page, @RequestParam(required = false) String size, @RequestParam(required = false) String target) {
//        Long userId = webSecurityContextt.getAuthenticationUserId();
        Long userId = 1l;
        DocumentsVo search = bookService.search(userId, query, page, size, target);
        return ResponseEntity.ok(ApiResponse.data(search));
    }

    @ApiVersion(1)
    @GetMapping("/book/{isbn}")
    public ResponseEntity<ApiResponse> get(@PathVariable String isbn) {
        Long authenticationUserId = webSecurityContextt.getAuthenticationUserId();
        DocumentsVo documentsVo = bookApiConnector.search(isbn, "1", "1", "isbn");
        return ResponseEntity.ok(ApiResponse.data(documentsVo));
    }

    @ApiVersion(1)
    @GetMapping("/book/recent")
    public ResponseEntity<ApiResponse> histories() {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        List<BookSearchHistory> bookSearchHistories = bookService.searchTop10Histories(userId);
        return ResponseEntity.ok(ApiResponse.data(bookSearchHistories));
    }

    @ApiVersion(1)
    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse> list() {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        List<Bookmark> bookmarks = bookService.getBookmarks(userId);
        return ResponseEntity.ok(ApiResponse.data(bookmarks.stream().map(bookmark -> bookmark.toVo()).collect(Collectors.toList())));
    }

    @ApiVersion(1)
    @PostMapping("/bookmark/{isbn}/title/{title}")
    public ResponseEntity<ApiResponse> mark(@PathVariable String isbn, @PathVariable String title) {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        return ResponseEntity.ok(ApiResponse.data(bookService.createBookmark(userId, isbn, title).toVo()));
    }

    @ApiVersion(1)
    @GetMapping("/bookmark/isbn/{isbn}")
    public ResponseEntity<ApiResponse> getBookmark(@PathVariable String isbn) {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        Optional<Bookmark> bookmark = Optional.ofNullable(bookService.getBookmark(userId, isbn));
        if (bookmark.isPresent()) {
            return ResponseEntity.ok(ApiResponse.data(bookmark.get().toVo()));
        } else {
            return ResponseEntity.ok(ApiResponse.data(null));
        }
    }

    @ApiVersion(1)
    @DeleteMapping("/bookmark/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        bookService.deleteBookmark(id);
        return ResponseEntity.ok().build();
    }
}
