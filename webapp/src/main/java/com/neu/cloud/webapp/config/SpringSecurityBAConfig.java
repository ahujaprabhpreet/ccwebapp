package com.neu.cloud.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SpringSecurityBAConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;


    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    protected void configure(HttpSecurity http) throws Exception {

        http.
                csrf().disable().
                authorizeRequests().
                antMatchers("/user/register").permitAll().
                anyRequest().authenticated().and().
                formLogin().disable().
                httpBasic().realmName("Realm").authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint());
    }
}
