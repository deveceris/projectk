package kr.co.eceris.projectk.config;

import kr.co.eceris.projectk.security.AccountUserDetailsService;
import kr.co.eceris.projectk.security.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.authorizeRequests().anyRequest().permitAll();


        http.authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/*.css").permitAll()
                .antMatchers("/*.js").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginProcessingUrl("/api/login")
//                .successHandler(new LoginSuccessHandler()).permitAll()

                .defaultSuccessUrl("/home").failureUrl("/").permitAll()
                .and().logout().permitAll();
    }
}
