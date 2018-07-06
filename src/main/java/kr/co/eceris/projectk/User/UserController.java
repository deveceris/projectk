package kr.co.eceris.projectk.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.ok(userRepository.findByIdIsNotNull());
    }

    @PostMapping("/user")
    public ResponseEntity<User> create(@RequestBody UserVo userVo) {
        User saved = userRepository.save(User.of(userVo));
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

}
