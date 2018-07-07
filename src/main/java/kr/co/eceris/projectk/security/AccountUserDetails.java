package kr.co.eceris.projectk.security;

import kr.co.eceris.projectk.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AccountUserDetails implements UserDetails {
    private static final Logger logger = LoggerFactory.getLogger(AccountUserDetails.class);
    private static final long serialVersionUID = 1L;
    User user;

    public AccountUserDetails(User account) {
        this.user = account;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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