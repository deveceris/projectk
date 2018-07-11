package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.user.User;
import kr.co.eceris.projectk.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public Bookmark createBookmark(Long userId, String isbn, String title) {
        User user = userService.get(userId);
        Bookmark bookmark = new Bookmark();
        bookmark.setIsbn(isbn.split(" ")[0]);
        bookmark.setTitle(title);
        bookmark.setUser(user);
        return bookmarkRepository.save(bookmark);
    }

    @Transactional(readOnly = true)
    public Bookmark getBookmark(Long userId, String isbn) {
        return bookmarkRepository.findByUserIdAndIsbn(userId, isbn);
    }

    @Transactional
    public void deleteBookmark(Long id) {
        bookmarkRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Bookmark> getBookmarks(Long userId) {
        return bookmarkRepository.findByUserId(userId);
    }

    @Transactional
    public BookSearchHistory createHistory(Long userId, String keyword) {
        BookSearchHistory history = new BookSearchHistory();
        history.setKeyword(keyword);
        history.setUserId(userId);
        return bookSearchHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    public List<BookSearchHistory> searchTop10Histories(Long userId) {
        return bookSearchHistoryRepository.findTop10ByUserIdOrderByIdDesc(userId);
    }

    public DocumentsVo search(Long userId, String query, String page, String size, String target, String sort) {
        return apiConnector.search(query, page, size, target, sort);
    }
}
