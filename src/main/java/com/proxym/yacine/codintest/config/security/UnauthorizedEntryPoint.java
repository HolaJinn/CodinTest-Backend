package com.proxym.yacine.codintest.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
@Slf4j
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException {
        try {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
        }
        catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}