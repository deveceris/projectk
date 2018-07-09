package kr.co.eceris.projectk.auth;

import kr.co.eceris.projectk.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Slf4j
public class AccountUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    User user;

    public AccountUserDetails(User account) {
        this.user = account;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList();
    }

    public Long getId() {
        return user.getId();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
