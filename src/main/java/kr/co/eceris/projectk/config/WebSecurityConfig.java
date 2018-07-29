package kr.co.eceris.projectk.config;

import kr.co.eceris.projectk.security.JWTAuthenticationFilter;
import kr.co.eceris.projectk.security.JWTLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource(name="userService")
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
//        .authorizeRequests().anyRequest().permitAll().and().headers().frameOptions().sameOrigin();
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/h2/**").permitAll()

                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/*.css").permitAll()
                .antMatchers("/*.js").permitAll()

                .antMatchers("/api/v1/user").permitAll()
                .antMatchers("/api/v1/user/**").permitAll()
                .antMatchers("/api/login").permitAll()

                .antMatchers("/login").permitAll()
                .antMatchers("/search").permitAll()
                .antMatchers("/bookmark").permitAll()
                .antMatchers("/book").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/api/config").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .addFilterBefore(new JWTLoginFilter("/api/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


    }

    /**
     * userDetailsService의 passwordEncoder 지정
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
