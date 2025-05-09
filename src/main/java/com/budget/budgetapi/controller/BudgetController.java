package com.budget.budgetapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.budget.budgetapi.dtos.UserAuth;
import com.budget.budgetapi.dtos.budgetDTO.BudgetDTO;
import com.budget.budgetapi.dtos.budgetDTO.ExposeBudget;
import com.budget.budgetapi.service.BudgetService;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    // Alle Budgets eines Benutzers anzeigen
    @GetMapping("/{userId}")
    public List<ExposeBudget> getBudgets(@PathVariable Long userId) {
        return budgetService.getBudgetsForUser(userId);
    }

    // Budget für einen Benutzer erstellen
    @PostMapping("createbudget/{userId}")
    public ExposeBudget createBudget(@PathVariable Long userId, @RequestBody BudgetDTO budgetDTO) throws IllegalAccessException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserAuth connectedUser = (UserAuth) auth.getPrincipal(); // fonctionne
        if(!connectedUser.getId().equals(userId)){
           throw new IllegalAccessException("Your aren't allowed to make this request");
        }
        return budgetService.createBudget(userId, budgetDTO);
    }

    @PostMapping("duplicate/{budgetId}")
    public ExposeBudget duplicateThisBudget(@PathVariable Long budgetId){
        return budgetService.duplicateThisBudget(budgetId);
    }
    
    

    // Budget eines Benutzers bearbeiten
    @PutMapping("/{userId}/{budgetId}")
    public ExposeBudget updateBudget(@PathVariable Long userId, @PathVariable Long budgetId, @RequestBody BudgetDTO budgetDTO) {
        return budgetService.updateBudget(userId, budgetId, budgetDTO);
    }

     // Budget eines Benutzers löschen
     @DeleteMapping("/{userId}/{budgetId}")
     public void deleteBudget(@PathVariable Long userId, @PathVariable Long budgetId){
        budgetService.deleteBudget(userId, budgetId);
     }
}
