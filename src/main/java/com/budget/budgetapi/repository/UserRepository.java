package com.budget.budgetapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.budget.budgetapi.model.User;

public interface  UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    
}
