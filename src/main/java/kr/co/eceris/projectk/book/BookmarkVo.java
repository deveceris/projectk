package kr.co.eceris.projectk.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkVo {
    private Long id;
    private String query;
    private int page;
    private int size;
    private String target;
    private String sort;
    private String isbn;
    private String barcode;
    private String publisher;
    private String title;

    public static BookmarkVo of(Bookmark bookmark) {
        return new BookmarkVo(bookmark.getId(), bookmark.getQuery(), bookmark.getPage(), bookmark.getSize(), bookmark.getTarget(), bookmark.getSort(),
                bookmark.getIsbn(), bookmark.getBarcode(), bookmark.getPublisher(), bookmark.getTitle());
    }
}
