package com.budget.budgetapi.service;


import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.budget.budgetapi.dtos.budgetDTO.BudgetDTO;
import com.budget.budgetapi.dtos.budgetDTO.ExposeBudget;
import com.budget.budgetapi.model.Budget;
import com.budget.budgetapi.model.BudgetEntry;
import com.budget.budgetapi.model.User;
import com.budget.budgetapi.repository.BudgetRepository;
import com.budget.budgetapi.repository.UserRepository;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    // show Budget of a defined user
    public List<ExposeBudget> getBudgetsForUser(Long userId) {
        List<ExposeBudget> result= new ArrayList<>();
        for(Budget budget: budgetRepository.findByUserId(userId)){
            result.add(new ExposeBudget(budget));
        }
        return result;
    }

    // create Budget 
    public ExposeBudget createBudget(Long userId, BudgetDTO budgetDTO) {
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User nicht gefunden"));
        Budget budget= new Budget(budgetDTO);
        budget.setUser(user);
        user.getBudgets().add(budget);
        userRepository.save(user);
        return new ExposeBudget(budgetRepository.save(budget));
    }

    // update Budget 
    public ExposeBudget updateBudget(Long userId, Long budgetId, BudgetDTO budget) {
        var existingBudget = budgetRepository.findById(budgetId).orElseThrow(() -> new RuntimeException("Budget nicht gefunden"));
        if (!existingBudget.getUser().getId().equals(userId)) {
            throw new RuntimeException("Zugriffsfehler: Budget gehört nicht diesem Benutzer");
        }
        existingBudget.setName(budget.getName());
        existingBudget.setMonth(Month.valueOf(budget.getMonth().toUpperCase()));
        existingBudget.setDescription(budget.getDescription()); 
        existingBudget.setLastModifiedDate(LocalDate.now());
        return new ExposeBudget(budgetRepository.save(existingBudget));
    }

    public ExposeBudget duplicateThisBudget(Long budgetId){
        Budget newBudget= new Budget();
        var altBudget = budgetRepository.findById(budgetId).orElseThrow(() -> new RuntimeException("Budget nicht gefunden"));
        newBudget.setName(altBudget.getName());
        newBudget.setUser(altBudget.getUser());
        newBudget.setDescription(altBudget.getDescription());
        newBudget.setCreationDate(LocalDate.now());
        newBudget.setMonth(altBudget.getMonth().plus(1));
        newBudget.setPlanedAmount(altBudget.getPlanedAmount());
        newBudget.setUsedAmount(0.0);
        List<BudgetEntry> entrys= newBudget.getBudgetentries();
        for(BudgetEntry altEntry: altBudget.getBudgetentries()){
            BudgetEntry newEntry = new BudgetEntry();
            newEntry.setBudget(newBudget);
            newEntry.setDescription(altEntry.getDescription());
            newEntry.setName(altEntry.getName());
            newEntry.setPlanedAmount(altEntry.getPlanedAmount());
            newEntry.setUsedAmount(0.0);
            newEntry.setType(altEntry.getType());
            entrys.add(newEntry);
        }
        return new ExposeBudget( budgetRepository.save(newBudget));
    }

    public void deleteBudget(Long userId, Long budgetId){
        var budget = budgetRepository.findById(budgetId).orElseThrow(() -> new RuntimeException("Budget nicht gefunden"));
        if (!budget.getUser().getId().equals(userId)) {
            throw new RuntimeException("Zugriffsfehler: Budget gehört nicht diesem Benutzer");
        }
        User user= budget.getUser();
        for(BudgetEntry budgetEntry: budget.getBudgetentries()){
            switch(budgetEntry.getType()){
                case INCOME -> user.setGlobalIncome(user.getGlobalIncome()-budgetEntry.getPlanedAmount());
                case INVESTMENT -> user.setGlobalInvestment(user.getGlobalInvestment()-budgetEntry.getPlanedAmount());
                case SAVING -> user.setGlobalSavings(user.getGlobalSavings()-budgetEntry.getPlanedAmount());
                case SPENDING -> {
                    user.setGlobalSpending(user.getGlobalSpending()-budgetEntry.getUsedAmount());
                    budget.setPlanedAmount(budget.getPlanedAmount()-budgetEntry.getPlanedAmount());
                    budget.setUsedAmount(budget.getUsedAmount()-budgetEntry.getUsedAmount());
                }
                default -> throw new IllegalArgumentException("Invalid Type");
            }
            userRepository.save(user);
        }
        
        budgetRepository.deleteById(budgetId);
    }
}

