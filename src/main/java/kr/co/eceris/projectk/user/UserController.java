package kr.co.eceris.projectk.user;

import kr.co.eceris.projectk.ApiResponse;
import kr.co.eceris.projectk.security.EncryptUtil;
import kr.co.eceris.projectk.config.ApiVersion;
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

    @ApiVersion(1)
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> list() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(ApiResponse.data(users.stream().map(user -> user.toVo()).collect(Collectors.toList())));
    }

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
        return ResponseEntity.ok(ApiResponse.data(saved.toVo()));
    }

    @ApiVersion(1)
    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse> get(@PathVariable Long id) {
        User saved = userService.get(id);
        return ResponseEntity.ok(ApiResponse.data(saved.toVo()));
    }

    @ApiVersion(1)
    @GetMapping("/user/username/{username}")
    public ResponseEntity<ApiResponse> get(@PathVariable String username) {
        Optional<User> saved = Optional.ofNullable(userService.get(username));
        if (saved.isPresent()) {
            return ResponseEntity.ok(ApiResponse.data(saved.get().toVo()));
        } else {
            return ResponseEntity.ok(ApiResponse.data(null));
        }
    }

    @GetMapping("/api/config")
    public ResponseEntity<ApiResponse> config(HttpServletRequest req) {
        String remoteAddr = req.getRemoteAddr();
        return ResponseEntity.ok(ApiResponse.data(remoteAddr));
    }

}
