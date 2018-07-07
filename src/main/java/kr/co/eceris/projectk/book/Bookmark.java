package kr.co.eceris.projectk.book;

import kr.co.eceris.projectk.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String title;

    /**
     * isbn 정보를 토대로 상세 조회(rest)
     */
    private String isbn;
}
