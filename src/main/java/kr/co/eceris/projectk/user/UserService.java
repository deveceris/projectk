package kr.co.eceris.projectk.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 사용자 목록 조회
     * @return
     */
    public List<User> getUsers() {
        return userRepository.findByIdIsNotNull();
    }

    /**
     * 사용자 생성
     * @param userVo
     * @return
     */
    public User create(UserVo userVo) {
        Optional<User> check = userRepository.findByUsername(userVo.getUsername());
        if (check.isPresent()) {
            throw new IllegalArgumentException("username duplicated.");
        }
        User target = User.of(userVo);
        target.setPassword(passwordEncoder.encode(userVo.getPassword()));
        return userRepository.save(target);
    }

    /**
     * 사용자 조회
     * @param id
     * @return
     */
    public User get(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("not found user");
        }
        return optionalUser.get();
    }

    /**
     * 사용자 조회(username)
     * @param username
     * @return
     */
    public User get(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isPresent()) {
            throw new IllegalArgumentException("not found user");
        }
        return user.get();
    }

}
