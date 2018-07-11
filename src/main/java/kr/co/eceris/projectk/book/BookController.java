package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.ApiResponse;
import kr.co.eceris.projectk.config.ApiVersion;
import kr.co.eceris.projectk.config.WebSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse> search(@RequestParam String query, @RequestParam(required = false) String page, @RequestParam(required = false) String size, @RequestParam(required = false) String target, @RequestParam(required = false) String sort) {
        DocumentsVo search = bookService.search(query, page, size, target, sort);
        return ResponseEntity.ok(ApiResponse.data(search));
    }

    @ApiVersion(1)
    @GetMapping("/book/inquiry")
    public ResponseEntity<ApiResponse> inquiry(@RequestParam String query, @RequestParam String page, @RequestParam String size, @RequestParam String target, @RequestParam String sort
            , @RequestParam(required = false) String isbn, @RequestParam(required = false) String barcode, @RequestParam(required = false) String publisher, @RequestParam(required = false) String title) {
        try {
            BookVo inquiry = bookService.inquiry(query, page, size, target, sort, isbn, barcode, publisher, title);
            return ResponseEntity.ok(ApiResponse.data(inquiry));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.message(e.getMessage()));
        }
    }

    @ApiVersion(1)
    @GetMapping("/histories")
    public ResponseEntity<ApiResponse> histories() {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        List<BookSearchHistory> bookSearchHistories = bookService.searchTop10Histories(userId);
        return ResponseEntity.ok(ApiResponse.data(bookSearchHistories));
    }

    @ApiVersion(1)
    @PostMapping("/history")
    public ResponseEntity<ApiResponse> createHistory(@RequestParam String keyword) {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        BookSearchHistory history = bookService.createHistory(userId, keyword);
        return ResponseEntity.ok(ApiResponse.data(history));
    }

    @ApiVersion(1)
    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse> bookmarks() {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        List<Bookmark> bookmarks = bookService.getBookmarks(userId);
        return ResponseEntity.ok(ApiResponse.data(bookmarks.stream().map(bookmark -> bookmark.toVo()).collect(Collectors.toList())));
    }

    @ApiVersion(1)
    @PostMapping("/bookmark")
    public ResponseEntity<ApiResponse> createBookmark(@RequestParam String query, @RequestParam int page, @RequestParam int size, @RequestParam String target, @RequestParam String sort
            , @RequestParam(required = false) String isbn, @RequestParam(required = false) String barcode, @RequestParam(required = false) String publisher, @RequestParam(required = false) String title) {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        return ResponseEntity.ok(ApiResponse.data(bookService.createBookmark(userId, query, page, size, target, sort, isbn, barcode, publisher, title).toVo()));
    }

    @ApiVersion(1)
    @GetMapping("/bookmark")
    public ResponseEntity<ApiResponse> getBookmark(@RequestParam String query, @RequestParam int page, @RequestParam int size, @RequestParam String target, @RequestParam String sort
            , @RequestParam(required = false) String isbn, @RequestParam(required = false) String barcode, @RequestParam(required = false) String publisher, @RequestParam(required = false) String title) {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        Optional<Bookmark> bookmark = Optional.ofNullable(bookService.getBookmark(userId, query, page, size, target, sort, isbn, barcode, publisher, title));
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
