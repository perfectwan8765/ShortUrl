package com.jsw.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    public void configure (WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/image/**");
    }

    @Override
    protected void configure (HttpSecurity http) throws Exception {
        // member URL 인증된 사용자만 접근
        http.authorizeRequests()
            .antMatchers("/**").permitAll()
            .antMatchers("/short/**").permitAll()
            .and().csrf().disable();

        // Logout
        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true);                                
    }

}