package kr.co.eceris.projectk.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContext extends SecurityContextHolder {

    public Authentication getAuthentication() {
        return getContext().getAuthentication();
    }

    public AccountUserDetails getAuthenticationUser() {
        return (AccountUserDetails) getContext().getAuthentication().getPrincipal();
    }

    public String getAuthenticationUserUsername() {
        AccountUserDetails user = (AccountUserDetails) getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

    public Long getAuthenticationUserId() {
        AccountUserDetails user = (AccountUserDetails) getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    public String getAuthenticationUserPassword() {
        AccountUserDetails user = (AccountUserDetails) getContext().getAuthentication().getPrincipal();
        return user.getPassword();
    }

}
