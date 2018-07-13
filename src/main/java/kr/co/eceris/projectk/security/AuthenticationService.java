package kr.co.eceris.projectk.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AuthenticationService {

    private static final long EXPIRATION_HOUR = 1;
    private static final String SECRET_KEY = "PrOjEcTtOkEnSeCrEtKeY";
    private static final String HEADER_NAME = "Authorization";
    private static final String TOKEN_PREFIX = "ProjectK";

    public static void addAuthentication(HttpServletResponse response, Authentication username) {
        AccountUserDetails accountUserDetails = (AccountUserDetails) username.getPrincipal();
        String jwt = Jwts.builder().setSubject(accountUserDetails.getUsername()).setId(accountUserDetails.getId().toString())
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(EXPIRATION_HOUR)))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
        response.addHeader(HEADER_NAME, TOKEN_PREFIX + jwt);
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_NAME);
        if (!StringUtils.isEmpty(token)) {
            // parse the token.
            Claims body = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String user = body.getSubject();
            String id = body.getId();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            authenticationToken.setDetails(id);
            return user != null ? authenticationToken : null;
        }
        return null;
    }

}
