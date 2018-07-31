package kr.co.eceris.projectk.user;

import kr.co.eceris.projectk.ApiResponse;
import kr.co.eceris.projectk.config.ApiVersion;
import kr.co.eceris.projectk.security.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 사용자 목록조회
     *
     * @return
     */
    @ApiVersion(1)
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> list() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(ApiResponse.data(users.stream().map(user -> UserVo.of(user)).collect(Collectors.toList())));
    }

    /**
     * 사용자 생성
     *
     * @param req
     * @param userVo
     * @return
     */
    @ApiVersion(1)
    @PostMapping("/user")
    public ResponseEntity<ApiResponse> create(HttpServletRequest req, @RequestBody UserVo userVo) {
        User saved = null;
        try {
            String password = EncryptUtil.decrypt(req.getRemoteAddr(), userVo.getPassword());
            userVo.setPassword(password);
            saved = userService.create(userVo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.message(e.getMessage()));
        }
        return ResponseEntity.ok(ApiResponse.data(UserVo.of(saved)));
    }

    /**
     * 사용자 조회
     *
     * @param id
     * @return
     */
    @ApiVersion(1)
    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse> get(@PathVariable Long id) {
        UserVo userVo = UserVo.of(userService.get(id));
        return ResponseEntity.ok(ApiResponse.data(userVo));
    }

    /**
     * 사용자 조회(username)
     *
     * @param username
     * @return
     */
    @ApiVersion(1)
    @GetMapping("/user/username/{username}")
    public ResponseEntity<ApiResponse> get(@PathVariable String username) {
        UserVo userVo = Optional.ofNullable(userService.get(username)).map(user -> UserVo.of(user)).orElse(null);
        return ResponseEntity.ok(ApiResponse.data(userVo));
    }

    /**
     * 암호화를 위한 설정 값조회
     *
     * @param req
     * @return
     */
    @ApiVersion(1)
    @GetMapping("/config")
    public ResponseEntity<ApiResponse> config(HttpServletRequest req) {
        String remoteAddr = req.getRemoteAddr();
        return ResponseEntity.ok(ApiResponse.data(remoteAddr));
    }

}
