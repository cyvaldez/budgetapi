package com.budget.budgetapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.budget.budgetapi.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
