package kr.co.eceris.projectk.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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
}
