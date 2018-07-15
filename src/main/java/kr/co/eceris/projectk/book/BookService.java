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

    @Transactional(readOnly = true)
    public Bookmark getBookmark(Long userId, String query, int page, int size, String target, String sort, String isbn, String barcode, String publisher, String title) {
        return bookmarkRepository.findByUserIdAndQueryAndPageAndSizeAndTargetAndSortAndIsbnAndBarcodeAndPublisherAndTitle(userId, query, page, size, target, sort, isbn, barcode, publisher, title);
    }

    @Transactional
    public void deleteBookmark(Long id) {
        bookmarkRepository.deleteById(id);
    }

    @Transactional
    public BookSearchHistory createHistory(Long userId, String keyword) {
        BookSearchHistory saved = bookSearchHistoryRepository.findByUserIdAndKeyword(userId, keyword);
        if (Optional.ofNullable(saved).isPresent()) {
            return saved;
        }
        BookSearchHistory history = new BookSearchHistory();
        history.setKeyword(keyword);
        history.setUserId(userId);
        return bookSearchHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    public List<BookSearchHistory> searchTop10Histories(Long userId) {
        return bookSearchHistoryRepository.findTop10ByUserIdOrderByIdDesc(userId);
    }

    public DocumentsVo search(String query, String page, String size, String target, String sort) {
        return apiConnector.search(query, page, size, target, sort);
    }

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
                        } else{
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
