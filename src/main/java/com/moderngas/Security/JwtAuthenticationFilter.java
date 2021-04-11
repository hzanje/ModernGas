package com.moderngas.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.LoginDto;
import com.moderngas.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo userRepo;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepo userRepo) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Grab credentials and map them to login view-model
        LoginDto credentials = null;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Create Login Token */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUserName(), credentials.getPassword(), new ArrayList<>());

        //Authenticate User
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        /* Grab Principal */
        UserDetailsImpl principal = (UserDetailsImpl) authResult.getPrincipal();

        /* Create JWT Token */
        /* If token already exist return same else create new token */
        UserEntity userEntity = userRepo.findByMobileNumber(Long.parseLong(principal.getUsername())).get();
        String token = userEntity.getToken();
        if (StringUtils.isEmpty(token)) {
            token = JWT.create()
                    .withSubject(principal.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
            saveUserToken(token, userEntity);
        }

        /* Add token in Response */
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
    }

    private void saveUserToken(String token, UserEntity userEntity) {
        userEntity.setToken(token);
        userRepo.save(userEntity);
    }
}
