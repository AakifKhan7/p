package com.Aakifkhan.Rentally.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Aakifkhan.Rentally.model.User.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByPhoneNumber(String phoneNumber);
}
