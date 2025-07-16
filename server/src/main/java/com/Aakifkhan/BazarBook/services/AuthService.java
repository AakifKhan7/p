package com.Aakifkhan.BazarBook.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Aakifkhan.BazarBook.dto.auth.AuthResponse;
import com.Aakifkhan.BazarBook.dto.auth.LoginRequest;
import com.Aakifkhan.BazarBook.dto.auth.SignupRequest;
import com.Aakifkhan.BazarBook.model.User.UserAuthModel;
import com.Aakifkhan.BazarBook.model.User.UserModel;
import com.Aakifkhan.BazarBook.repository.UserAuthRepository;
import com.Aakifkhan.BazarBook.repository.UserRepository;
import com.Aakifkhan.BazarBook.security.JwtTokenProvider;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse registerUser(SignupRequest signUpRequest) {
        if (userAuthRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email address already in use.");
        }

        // Create user profile (self-audited)
        UserModel user = new UserModel();
        user.setName(signUpRequest.getName());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setCreatedBy(user); // self reference
        user.setUpdatedBy(user);
        userRepository.save(user);

        // Create auth entry
        UserAuthModel auth = new UserAuthModel();
        auth.setEmail(signUpRequest.getEmail());
        auth.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        auth.setUser(user);
        auth.setCreatedBy(user);
        auth.setUpdatedBy(user);
        userAuthRepository.save(auth);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword()));

        String token = tokenProvider.generateToken(authentication);
        return new AuthResponse(token);
    }

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        String token = tokenProvider.generateToken(authentication);
        return new AuthResponse(token);
    }
}
