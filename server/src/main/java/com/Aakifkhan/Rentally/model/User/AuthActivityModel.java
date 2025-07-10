package com.Aakifkhan.Rentally.model.User;

import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AuthActivity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthActivityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_auth_id", nullable = false)
    private UserAuthModel userAuth;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private Timestamp loginAt;

    private Timestamp logoutAt;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private Timestamp updatedAt;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createdAt = now;
        updatedAt = now;
        if (loginAt == null) loginAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
