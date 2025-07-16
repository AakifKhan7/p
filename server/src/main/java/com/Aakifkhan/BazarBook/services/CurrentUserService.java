package com.Aakifkhan.BazarBook.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Aakifkhan.BazarBook.model.User.UserModel;
import com.Aakifkhan.BazarBook.repository.UserRepository;
import com.Aakifkhan.BazarBook.security.CustomUserDetails;

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
