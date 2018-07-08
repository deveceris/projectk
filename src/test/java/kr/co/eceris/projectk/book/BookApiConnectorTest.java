package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.TestContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BookApiConnectorTest extends TestContext {

    private BookApiConnector bookApiConnector;

    @Before
    public void init() {
        bookApiConnector = context.getBean(BookApiConnector.class);
    }

    @Test
    public void 개미_데이터_10개_잘가져오나() {
        DocumentsVo result = bookApiConnector.search("개미", null, "1", "10", null, null);
        assertTrue("Success for select Documents", result.getDocuments().size() > 0);
    }

    @Test
    public void isbn으로_1개_조회() {
        DocumentsVo result = bookApiConnector.search("8932903492", null, "1", "1", "isbn", null);
        assertTrue("Success for select Documents", result.getDocuments().size() == 1);
    }
}
