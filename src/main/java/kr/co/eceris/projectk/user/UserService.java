package kr.co.eceris.projectk.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getUsers() {
        return userRepository.findByIdIsNotNull();
    }

    public User create(UserVo userVo) {
        User check = userRepository.findByUsername(userVo.getUsername());
        if (check != null) {
            throw new IllegalArgumentException("username duplicated.");
        }
        User target = User.of(userVo);
        target.setPassword(passwordEncoder.encode(userVo.getPassword()));
        return userRepository.save(target);
    }

    public User get(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("not found user");
        }
        return optionalUser.get();
    }

    public User get(String username) {
        return userRepository.findByUsername(username);
    }

}
