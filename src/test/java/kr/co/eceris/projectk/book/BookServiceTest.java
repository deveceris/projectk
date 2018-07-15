package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.TestContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BookServiceTest extends TestContext {
    private BookService bookService;

    @Before
    public void init() {
        bookService = context.getBean(BookService.class);
    }

    @Test
    public void 북마크생성() {
        String title = "개미";
        Bookmark bookmark = bookService.createBookmark(1l, title, 1, 10, "all", "accuracy", "isbn", "바코드", "출판사", title);
        Assert.assertTrue("is same title", title.equals(bookmark.getTitle()));
    }

    @Test
    public void 북마크삭제() {
        Bookmark bookmark = bookService.createBookmark(1l, "개미", 1, 10, "all", "accuracy", "isbn", "바코드", "출판사", "개미");
        bookService.deleteBookmark(bookmark.getId());
        boolean condition = bookService.getBookmarks(1l).stream().anyMatch(b -> b.getId() == bookmark.getId());
        Assert.assertFalse("is any match id", condition);
    }

    @Test
    public void 북마크목록() {
        bookService.createBookmark(1l, "개미1", 1, 10, "all", "accuracy", "isbn", "바코드", "출판사1", "개미1");
        bookService.createBookmark(1l, "개미2", 2, 10, "all", "accuracy", "isbn", "바코드", "출판사2", "개미2");
        bookService.createBookmark(1l, "개미3", 3, 10, "all", "accuracy", "isbn", "바코드", "출판사3", "개미3");
        bookService.createBookmark(1l, "개미4", 4, 10, "all", "accuracy", "isbn", "바코드", "출판사4", "개미4");
        bookService.createBookmark(1l, "개미5", 5, 10, "all", "accuracy", "isbn", "바코드", "출판사5", "개미5");

        Assert.assertTrue("is same count", bookService.getBookmarks(1l).size() == 5);
    }
}
