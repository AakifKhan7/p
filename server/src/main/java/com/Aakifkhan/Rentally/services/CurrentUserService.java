package com.Aakifkhan.Rentally.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Aakifkhan.Rentally.model.User.UserModel;
import com.Aakifkhan.Rentally.repository.UserRepository;
import com.Aakifkhan.Rentally.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public UserModel getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails details)) {
            throw new RuntimeException("Unauthenticated");
        }
        return userRepository.findById(details.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
