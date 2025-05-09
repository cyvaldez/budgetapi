package com.budget.budgetapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.budget.budgetapi.model.BudgetEntry;


public interface BudgetEntryRepository extends JpaRepository<BudgetEntry, Long> {
    List<BudgetEntry> findByBudgetId(Long budgetId);
}