package kr.co.eceris.projectk.user;

import com.google.common.collect.Lists;
import kr.co.eceris.projectk.book.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Bookmark> bookmark;

    public UserVo toVo() {
        return new UserVo(this.id, this.username);
    }

    public static User of(UserVo vo) {
        return new User(vo.getId(), vo.getUsername(), vo.getPassword(), Lists.newArrayList());
    }
}
