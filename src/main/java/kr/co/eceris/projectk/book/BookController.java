package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.ApiResponse;
import kr.co.eceris.projectk.config.ApiVersion;
import kr.co.eceris.projectk.config.WebSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private WebSecurityContext webSecurityContextt;


    /**
     * 도서 검색
     *
     * @param query 검색어
     * @param page 페이지
     * @param size 페이지 당 갯수
     * @param target 검색 조건
     * @param sort 정렬방식
     * @return
     */
    @ApiVersion(1)
    @GetMapping("/book/search")
    public ResponseEntity<ApiResponse> searchV1(@RequestParam String query, @RequestParam(required = false) String page, @RequestParam(required = false) String size, @RequestParam(required = false) String target, @RequestParam(required = false) String sort) {
        DocumentsVo search = bookService.search(query, page, size, target, sort);
        return ResponseEntity.ok(ApiResponse.data(search));
    }

    /**
     * 도서 검색(non block)
     *
     * @param query 검색어
     * @param page 페이지
     * @param size 페이지 당 갯수
     * @param target 검색 조건
     * @param sort 정렬방식
     * @return
     */
    @ApiVersion(2)
    @GetMapping("/book/search")
    public Mono<ApiResponse> searchV2(@RequestParam String query, @RequestParam(required = false) String page, @RequestParam(required = false) String size, @RequestParam(required = false) String target, @RequestParam(required = false) String sort) {
        Mono<DocumentsVo> documentsVoMono = bookService.searchV2(query, page, size, target, sort);
        return documentsVoMono.map(documentsVo -> ApiResponse.data(documentsVo));
    }

    /**
     * 도서 상세 조회
     * (식별자로 ISBN은 충분치 않아, 복합식별자로 아래 파라미터들을 사용)
     * @param query
     * @param page
     * @param size
     * @param target
     * @param sort
     * @param isbn
     * @param barcode
     * @param publisher
     * @param title
     * @return
     */
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

    /**
     * 검색 히스토리 조회
     * @return
     */
    @ApiVersion(1)
    @GetMapping("/histories")
    public ResponseEntity<ApiResponse> histories() {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        List<BookSearchHistory> bookSearchHistories = bookService.searchTop10Histories(userId);
        return ResponseEntity.ok(ApiResponse.data(bookSearchHistories));
    }

    /**
     * 검색 히스토리 생성
     * @param keyword
     * @return
     */
    @ApiVersion(1)
    @PostMapping("/history")
    public ResponseEntity<ApiResponse> createHistory(@RequestParam String keyword) {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        BookSearchHistory history = bookService.createHistory(userId, keyword);
        return ResponseEntity.ok(ApiResponse.data(history));
    }

    /**
     * 북마크 목록 조회
     * @return
     */
    @ApiVersion(1)
    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse> bookmarks() {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        List<Bookmark> bookmarks = bookService.getBookmarks(userId);
        return ResponseEntity.ok(ApiResponse.data(bookmarks.stream().map(bookmark -> BookmarkVo.of(bookmark)).collect(Collectors.toList())));
    }

    /**
     * 북마크 생성
     * @param query
     * @param page
     * @param size
     * @param target
     * @param sort
     * @param isbn
     * @param barcode
     * @param publisher
     * @param title
     * @return
     */
    @ApiVersion(1)
    @PostMapping("/bookmark")
    public ResponseEntity<ApiResponse> createBookmark(@RequestParam String query, @RequestParam int page, @RequestParam int size, @RequestParam String target, @RequestParam String sort
            , @RequestParam(required = false) String isbn, @RequestParam(required = false) String barcode, @RequestParam(required = false) String publisher, @RequestParam(required = false) String title) {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        return ResponseEntity.ok(ApiResponse.data(BookmarkVo.of(bookService.createBookmark(userId, query, page, size, target, sort, isbn, barcode, publisher, title))));
    }

    /**
     * 북마크 조회
     * @param query
     * @param page
     * @param size
     * @param target
     * @param sort
     * @param isbn
     * @param barcode
     * @param publisher
     * @param title
     * @return
     */
    @ApiVersion(1)
    @GetMapping("/bookmark")
    public ResponseEntity<ApiResponse> getBookmark(@RequestParam String query, @RequestParam int page, @RequestParam int size, @RequestParam String target, @RequestParam String sort
            , @RequestParam(required = false) String isbn, @RequestParam(required = false) String barcode, @RequestParam(required = false) String publisher, @RequestParam(required = false) String title) {
        Long userId = webSecurityContextt.getAuthenticationUserId();
        Optional<Bookmark> bookmark = Optional.ofNullable(bookService.getBookmark(userId, query, page, size, target, sort, isbn, barcode, publisher, title));
        if (bookmark.isPresent()) {
            return ResponseEntity.ok(ApiResponse.data(BookmarkVo.of(bookmark.get())));
        } else {
            return ResponseEntity.ok(ApiResponse.data(null));
        }
    }

    /**
     * 북마크 삭제
     * @param id
     * @return
     */
    @ApiVersion(1)
    @DeleteMapping("/bookmark/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        bookService.deleteBookmark(id);
        return ResponseEntity.ok().build();
    }
}
