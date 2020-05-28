package com.moderngas.Security;

import com.moderngas.jpaentity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsImpl implements UserDetails {

    private String userName;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserDetailsImpl() {
    }

    public UserDetailsImpl(UserEntity userEntity) {
        this.userName = String.valueOf(userEntity.getMobileNumber());
        this.password = userEntity.getPassword();
        /*this.authorities = Arrays.stream(userEntity.getRoles().split(","))
                .map(SimpleGrantedAuthority :: new)
                .collect(Collectors.toList());*/
    }

    /*@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
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
