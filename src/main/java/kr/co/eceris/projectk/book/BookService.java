package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.user.User;
import kr.co.eceris.projectk.user.UserService;
import kr.co.eceris.projectk.user.UserVo;
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
        bookmark.setIsbn(isbn);
        bookmark.setTitle(title);
        bookmark.setUser(user);
        return bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void deleteBookmark(Long id) {
        bookmarkRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Bookmark> getBookmarks() {
        return bookmarkRepository.findByIdIsNotNull();
    }

    @Transactional(readOnly = true)
    public List<BookSearchHistory> searchTop10Histories(Long userId) {
        return bookSearchHistoryRepository.findTop10ByUserId(userId);
    }

    @Transactional
    public DocumentsVo search(Long userId, String query, String sort, String page, String size, String target, String category) {
        BookSearchHistory bookSearchHistory = new BookSearchHistory();
        bookSearchHistory.setKeyword(query);
        bookSearchHistory.setUserId(userId);
        bookSearchHistoryRepository.save(bookSearchHistory);
        return apiConnector.search(query, sort, page, size, target, category);
    }
}
