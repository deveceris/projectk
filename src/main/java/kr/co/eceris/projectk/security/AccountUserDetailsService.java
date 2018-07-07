package kr.co.eceris.projectk.security;

import kr.co.eceris.projectk.user.User;
import kr.co.eceris.projectk.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = repository.findByUsername(loginId);
        if (user == null) {
            throw new UsernameNotFoundException(loginId);
        }
        return new AccountUserDetails(user);
    }
}
