package kr.co.eceris.projectk.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.eceris.projectk.user.UserVo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	public JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	/** 인증 수행
	 * @param request
	 * @param response
	 * @return
	 * @throws AuthenticationException
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		UserVo user = new ObjectMapper().readValue(request.getInputStream(), UserVo.class);
        String password = EncryptUtil.decrypt(request.getRemoteAddr(), user.getPassword());
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
				password, Collections.emptyList()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
											Authentication auth) throws IOException, ServletException {
		AuthenticationService.addAuthentication(response, auth);
	}

}
