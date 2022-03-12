package com.moderngas.security;

import com.moderngas.jpaentity.UserEntity;
import com.moderngas.jpaentity.UserRoleEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsImpl implements UserDetails {

    private String userName;
    private String password;
    private boolean active;
    private Set<GrantedAuthority> authorities;

    public UserDetailsImpl() {
    }

    public UserDetailsImpl(String userName, String password, boolean active, Set<GrantedAuthority> authorities) {
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.authorities = authorities;
    }

    public UserDetailsImpl(UserEntity userEntity) {
        this.userName = String.valueOf(userEntity.getMobileNumber());
        this.password = userEntity.getPassword();
        this.active = userEntity.isActiveFlag();
        this.authorities = userEntity.getRoleEntitySet().stream().map(UserRoleEntity::getRole)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
