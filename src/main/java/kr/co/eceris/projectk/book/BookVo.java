package kr.co.eceris.projectk.book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookVo {
    private List<String> authors;
    private String barcode;
    private String category;
    private String contents;
    private Date datetime;
    private String ebook_barcode;
    private String isbn;
    private String price;
    private String publisher;
    private String sale_price;
    private String sale_yn;
    private String status;
    private String thumbnail;
    private String title;
    private List<String> translators;
    private String url;
}
