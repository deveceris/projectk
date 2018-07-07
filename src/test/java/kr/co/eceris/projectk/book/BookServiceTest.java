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
        Bookmark bookmark = bookService.createBookmark(1l, "test1", title);
        Assert.assertTrue("is same title", title.equals(bookmark.getTitle()));
    }

    @Test
    public void 북마크삭제() {
        Bookmark bookmark = bookService.createBookmark(1l, "test2", "개미");
        bookService.deleteBookmark(bookmark.getId());
        boolean condition = bookService.getBookmarks().stream().anyMatch(b -> b.getId() == bookmark.getId());
        Assert.assertFalse("is any match id", condition);
    }

    @Test
    public void 북마크목록() {
        bookService.createBookmark(1l, "test3", "개미1");
        bookService.createBookmark(1l, "test4", "개미2");
        bookService.createBookmark(1l, "test5", "개미3");
        bookService.createBookmark(1l, "test6", "개미4");
        bookService.createBookmark(1l, "test7", "개미5");

        Assert.assertTrue("is same count", bookService.getBookmarks().size() == 5);
    }
}
