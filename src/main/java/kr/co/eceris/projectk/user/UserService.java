package kr.co.eceris.projectk.user;

import kr.co.eceris.projectk.security.AccountUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 사용자 목록 조회
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findByIdIsNotNull();
    }

    /**
     * 사용자 생성
     *
     * @param userVo
     * @return
     */
    @Transactional
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
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public User get(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 사용자 조회(username)
     *
     * @param username
     * @return
     */
    @Transactional(readOnly = true)
    public User get(String username) {
        return userRepository
                .findByUsername(username)
                .orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.get(username);
        return new AccountUserDetails(user);
    }
}
