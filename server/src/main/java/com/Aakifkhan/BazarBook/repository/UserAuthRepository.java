package com.Aakifkhan.BazarBook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Aakifkhan.BazarBook.model.User.UserAuthModel;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuthModel, Long> {
    Optional<UserAuthModel> findByEmail(String email);
}
