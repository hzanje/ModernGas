package com.moderngas.security;

import com.moderngas.jpaentity.UserEntity;
import com.moderngas.jpaentity.UserPrivilegeEntity;
import com.moderngas.jpaentity.UserRoleEntity;
import com.moderngas.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepo.findByMobileNumber(Long.parseLong(userName))
                .orElseThrow(() -> new UsernameNotFoundException("For User : " + userName));

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (UserRoleEntity userRole : userEntity.getRoleEntitySet()) {
            authorities.add(new SimpleGrantedAuthority(userRole.getRole()));
            for (UserPrivilegeEntity userPrivilege : userRole.getUserPrivilegeSet()) {
                authorities.add(new SimpleGrantedAuthority(userPrivilege.getPrivilege()));
            }
        }
        return new UserDetailsImpl(userEntity.getMobileNumber().toString(), userEntity.getPassword(),
                userEntity.isActiveFlag(), authorities);
    }
}
