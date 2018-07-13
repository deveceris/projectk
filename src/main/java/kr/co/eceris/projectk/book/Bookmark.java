package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    /**
     * 아래 정보를 토대로 상세 조회(rest)
     */
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
