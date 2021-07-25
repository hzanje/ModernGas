package com.moderngas.security;

import com.moderngas.jpaentity.UserEntity;
import com.moderngas.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = this.userRepo.findByMobileNumber(Long.parseLong(userName));
        userEntity.orElseThrow(() -> new UsernameNotFoundException("For User : " + userName));
        return userEntity.map(UserDetailsImpl::new).get();
    }
}
