package com.Aakifkhan.Rentally.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Aakifkhan.Rentally.model.User.UserAuthModel;
import com.Aakifkhan.Rentally.repository.UserAuthRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthRepository userAuthRepository;

    @Autowired
    public CustomUserDetailsService(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuthModel userAuth = userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetails(userAuth);
    }
}
