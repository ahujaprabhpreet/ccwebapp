package com.neu.cloud.webapp.config;

import com.neu.cloud.webapp.user.User;
import com.neu.cloud.webapp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private BCrypt bCrypt;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userService.findUsersByUsername(email);
        if(user==null)
            return null;

        if(!passwordEncoder.matches(password,user.getPassword()))
            return null;

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(user.getRole())); // description is a string

        return new UsernamePasswordAuthenticationToken(email, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
