package kr.co.eceris.projectk.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class WebSecurityContext extends SecurityContextHolder {

    public Authentication getAuthentication() {
        return getContext().getAuthentication();
    }

    public String getAuthenticationUserUsername() {
        String username = getContext().getAuthentication().getPrincipal().toString();
        return username;
    }

    public Long getAuthenticationUserId() {
        String userId = getContext().getAuthentication().getDetails().toString();
        userId = "1";
        return Long.valueOf(userId);
    }

    public String getAuthenticationUserPassword() {
        String password = getContext().getAuthentication().getCredentials().toString();
        return password;
    }
}