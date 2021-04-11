package com.moderngas.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.repository.UserRepo;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private UserRepo userRepo;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserRepo userRepo) {
        super(authenticationManager);
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Read the Authorization header, where the JWT token should be
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        // If header does not contain BEARER or is null delegate to Spring impl and exit
            if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // If header is present, try grab user principal from database and perform authorization
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue filter execution
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        try {
            if (token != null && isTokenExist(token)) {
                // parse the token and validate it
                String userName = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                        .build()
                        .verify(token)
                        .getSubject();

                // Search in the DB if we find the user by token subject (username)
                // If so, then grab user details and create spring auth token using username, pass, authorities/roles
                if (userName != null) {
                    UserEntity userEntity = userRepo.findByMobileNumber(Long.parseLong(userName)).get();
                    UserDetailsImpl userDetails = new UserDetailsImpl(userEntity);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userName, null, userDetails.getAuthorities());
                    return auth;
                }
                return null;
            }
        } catch (ExpiredJwtException | TokenExpiredException expiredException) {
            String requestURL = request.getRequestURL().toString();
            if (requestURL.contains("refreshToken") ||
                    requestURL.contains("logout")) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        null, null, null);
                return auth;
            }
            throw new TokenExpiredException("");
        } catch (Exception exception) {
            return (Authentication) exception;
        }
        return null;
    }

    private boolean isTokenExist(String token) {
        return userRepo.isTokenExist(token);
    }

}
