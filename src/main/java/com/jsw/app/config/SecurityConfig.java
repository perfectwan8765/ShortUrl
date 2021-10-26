package com.jsw.app.config;

import com.jsw.app.handler.MemberAuthFailureHandler;
import com.jsw.app.handler.MemberAuthSuccessHandler;
import com.jsw.app.util.MemberAuthenticationProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //@Autowired
    //private MemberService memberService;

    @Autowired
    private MemberAuthenticationProvider authenticationProvider;

    @Autowired
    private MemberAuthSuccessHandler authenticationSuccessHandler;

    @Autowired
    private MemberAuthFailureHandler authenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
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
            //.antMatchers("/member/**").authenticated()
            .and().csrf().disable();

        http.formLogin()
            .loginProcessingUrl("/member/login")
            .defaultSuccessUrl("/")
            .usernameParameter("email")
            .passwordParameter("password")
            .successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler)
            .and()
            .authenticationProvider(authenticationProvider);

        // Logout
        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true);                                
    }
    /*
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
        //auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
    */

}