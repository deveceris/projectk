package kr.co.eceris.projectk.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private Long id;

    private String username;
    private String password;

    public UserVo toVo() {
        return new UserVo(this.id, this.username);
    }

    public static User of(UserVo vo) {
        return new User(vo.getId(), vo.getUsername(), vo.getPassword());
    }
}
