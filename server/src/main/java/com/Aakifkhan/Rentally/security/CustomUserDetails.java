package com.Aakifkhan.Rentally.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Aakifkhan.Rentally.model.User.UserAuthModel;

public class CustomUserDetails implements UserDetails {

    private final UserAuthModel userAuth;

    public CustomUserDetails(UserAuthModel userAuth) {
        this.userAuth = userAuth;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: map real roles when implemented
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return userAuth.getPassword();
    }

    @Override
    public String getUsername() {
        return userAuth.getEmail();
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
        return !userAuth.isDeleted();
    }

    public Long getUserId() {
        return userAuth.getUser().getId();
    }
}
