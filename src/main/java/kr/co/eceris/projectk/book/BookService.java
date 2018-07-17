package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.user.User;
import kr.co.eceris.projectk.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BookApiConnector apiConnector;

    @Autowired
    private BookSearchHistoryRepository bookSearchHistoryRepository;

    @Autowired
    private UserService userService;

    /**
     * 북마크 목록 조회
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Bookmark> getBookmarks(Long userId) {
        return bookmarkRepository.findByUserIdOrderByIdDesc(userId);
    }

    @Transactional
    public Bookmark createBookmark(Long userId, String query, int page, int size, String target, String sort, String isbn, String barcode, String publisher, String title) {
        User user = userService.get(userId);
        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setQuery(query);
        bookmark.setPage(page);
        bookmark.setSize(size);
        bookmark.setTarget(target);
        bookmark.setSort(sort);
        bookmark.setIsbn(isbn);
        bookmark.setBarcode(barcode);
        bookmark.setPublisher(publisher);
        bookmark.setTitle(title);
        return bookmarkRepository.save(bookmark);
    }

    /**
     * 북마크 조회
     *
     * @param userId    사용자아이디
     * @param query     검색쿼리(복합 식별자)
     * @param page      페이지(복합 식별자)
     * @param size      페이지당 아이템 갯수(복합 식별자)
     * @param target    검색 조건(복합 식별자)
     * @param sort      정렬방식(복합 식별자)
     * @param isbn      ISBN(복합 식별자)
     * @param barcode   바코드(복합 식별자)
     * @param publisher 출판사(복합 식별자)
     * @param title     책 제목(복합 식별자)
     * @return
     */
    @Transactional(readOnly = true)
    public Bookmark getBookmark(Long userId, String query, int page, int size, String target, String sort, String isbn, String barcode, String publisher, String title) {
        return bookmarkRepository
                .findByUserIdAndQueryAndPageAndSizeAndTargetAndSortAndIsbnAndBarcodeAndPublisherAndTitle(userId, query, page, size, target, sort, isbn, barcode, publisher, title)
                .orElse(null);
    }

    /**
     * 북마크 삭제
     *
     * @param id
     */
    @Transactional
    public void deleteBookmark(Long id) {
        bookmarkRepository.deleteById(id);
    }

    /**
     * 검색 히스토리 생성
     *
     * @param userId
     * @param keyword
     * @return
     */
    @Transactional
    public BookSearchHistory createHistory(Long userId, String keyword) {
        return bookSearchHistoryRepository
                .findByUserIdAndKeyword(userId, keyword)
                .orElseGet(() -> {
                    BookSearchHistory history = new BookSearchHistory();
                    history.setKeyword(keyword);
                    history.setUserId(userId);
                    return bookSearchHistoryRepository.save(history);
                });
    }

    /**
     * 최근 10개의 검색 히스토리 조회
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<BookSearchHistory> searchTop10Histories(Long userId) {
        return bookSearchHistoryRepository.findTop10ByUserIdOrderByIdDesc(userId);
    }

    /**
     * 책 검색
     *
     * @param query  검색어
     * @param page   페이지
     * @param size   페이지당 갯수
     * @param target 검색조건
     * @param sort   정렬
     * @return
     */
    public DocumentsVo search(String query, String page, String size, String target, String sort) {
        return apiConnector.search(query, page, size, target, sort);
    }

    /**
     * 책 상세 검색
     *
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
    public BookVo inquiry(String query, String page, String size, String target, String sort, String isbn, String barcode, String publisher, String title) {
        DocumentsVo search = apiConnector.search(query, page, size, target, sort);
        Optional<BookVo> optionalBookVo = search.getDocuments().stream().filter(bookVo -> {
                    if (!StringUtils.isEmpty(bookVo.getIsbn()) && bookVo.getIsbn().equals(isbn)) {
                        return true;
                    } else if (!StringUtils.isEmpty(bookVo.getBarcode()) && bookVo.getBarcode().equals(barcode)) {
                        return true;
                    } else if (!StringUtils.isEmpty(bookVo.getPublisher()) && bookVo.getPublisher().equals(publisher)) {
                        if (!StringUtils.isEmpty(bookVo.getTitle()) && bookVo.getTitle().equals(title)) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
        ).findFirst();
        if (!optionalBookVo.isPresent()) {
            throw new IllegalArgumentException("not found error.");
        }

        return optionalBookVo.get();
    }
}
