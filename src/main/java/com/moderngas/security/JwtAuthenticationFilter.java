package com.moderngas.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moderngas.constants.ExceptionConstants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.jpaentity.UserTokenEntity;
import com.moderngas.pojo.LoginDto;
import com.moderngas.repository.UserRepo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.CollectionUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
        LoginDto credentials = new LoginDto();
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

    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        /* Grab Principal */
        UserDetailsImpl principal = (UserDetailsImpl) authResult.getPrincipal();

        /* Create JWT Token */
        /* If token already exist return same else create new token */
        UserEntity userEntity = userRepo.findByMobileNumber(Long.parseLong(principal.getUsername())).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_REGISTER_USER));
        String token = AESUtil.createJWTToken(principal.getUsername());
        Date expiredDate = new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME);
        saveUserToken(token, expiredDate, userEntity);

        /* Add token in Response */
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
    }

    private void saveUserToken(String token, Date expiredDate, UserEntity userEntity) {
        UserTokenEntity tokenEntity = new UserTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setExpiredDate(expiredDate);

        Set<UserTokenEntity> tokenEntitySet = userEntity.getUserTokenSet();
        if (CollectionUtils.isEmpty(tokenEntitySet)) {
            tokenEntitySet = new HashSet<>();
        }
        tokenEntitySet.add(tokenEntity);
        userEntity.setUserTokenSet(tokenEntitySet);
        userRepo.save(userEntity);
    }
}
