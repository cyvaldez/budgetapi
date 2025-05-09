package com.budget.budgetapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.budget.budgetapi.model.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserId(Long userId);
}