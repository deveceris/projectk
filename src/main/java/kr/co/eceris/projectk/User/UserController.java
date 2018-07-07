package kr.co.eceris.projectk.user;

import kr.co.eceris.projectk.config.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiVersion(1)
    @GetMapping("/users")
    public ResponseEntity<List<UserVo>> list() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users.stream().map(user -> user.toVo()).collect(Collectors.toList()));
    }

    @ApiVersion(1)
    @PostMapping("/user")
    public ResponseEntity<UserVo> create(@RequestBody UserVo userVo) {
        User saved = userService.create(userVo);
        return ResponseEntity.ok(saved.toVo());
    }

    @ApiVersion(1)
    @GetMapping("/user/{id}")
    public ResponseEntity<UserVo> get(@PathVariable Long id) {
        User saved = userService.get(id);
        return ResponseEntity.ok(saved.toVo());
    }

}
