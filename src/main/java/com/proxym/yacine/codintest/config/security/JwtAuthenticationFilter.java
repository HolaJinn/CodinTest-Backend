package com.proxym.yacine.codintest.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String HEADER_STRING;

    @Value("${jwt.token.prefix}")
    private String TOKEN_PREFIX;

    @Resource
    private UserDetailsService userDetailsService;

    @Autowired @Lazy
    private TokenProvider jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX,"");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            }
            catch (Exception e){
                logger.error(e.getMessage());
                Map<String,String> error = new HashMap<>() ;
                error.put("message",e.getMessage()) ;
                switch ( e.getClass().getSimpleName() ){
                    case "ExpiredJwtException" : {
                        error.put("error","TOKEN EXPIRED");
                        break;
                    }
                    case "BadCredentialsException" : {
                        error.put("error","BAD CREDENTIALS");
                        break;
                    }
                    default: {
                        error.put("error",e.getClass().getSimpleName());
                        break;
                    }
                }
                res.setContentType(APPLICATION_JSON_VALUE);
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                new ObjectMapper().writeValue(res.getOutputStream(),error);
            }
        }
        if ( username != null ) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthenticationToken(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch (Exception e){
                Map<String,String> error = new HashMap<>() ;
                error.put("error",e.getMessage());
                error.put("message",e.getMessage()) ;
                res.setContentType(APPLICATION_JSON_VALUE);
                res.setStatus(401);
                new ObjectMapper().writeValue(res.getOutputStream(),error);
                logger.error(e.getMessage());
            }
        }
        try {
            chain.doFilter(req, res);
        }
        catch (Exception e){
            logger.warn(e.getMessage());
        }
    }
}

