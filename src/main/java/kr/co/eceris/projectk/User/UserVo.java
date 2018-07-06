package kr.co.eceris.projectk.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private Long id;
    private String username;
    private String password;

    public UserVo(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
