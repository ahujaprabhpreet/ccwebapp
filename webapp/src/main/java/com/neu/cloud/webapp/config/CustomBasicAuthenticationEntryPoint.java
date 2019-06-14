package com.neu.cloud.webapp.config;

import com.neu.cloud.webapp.response.CustomResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        
        PrintWriter writer = response.getWriter();
        writer.println("{");
        writer.println("\"" + "HTTP Status 401" + "\""+ ":" +"\""+ " You are not logged in" +"\"");
        writer.println("}");



    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("Realm");
        super.afterPropertiesSet();
    }
}
