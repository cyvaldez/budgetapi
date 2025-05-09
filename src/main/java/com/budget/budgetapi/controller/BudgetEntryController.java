package com.budget.budgetapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.budget.budgetapi.dtos.budgetEntryDTO.BudgetEntryDTO;
import com.budget.budgetapi.dtos.budgetEntryDTO.ExposeEntry;
import com.budget.budgetapi.service.BudgetEntryService;

@RestController
@RequestMapping("/api/budgets/entries")
public class BudgetEntryController {

    @Autowired
    private BudgetEntryService budgetEntryService;

    // BudgetEntry für einen Budget erstellen
    @PostMapping("/{budgetId}")
    public ExposeEntry createBudgetEntry(@PathVariable Long budgetId, @RequestBody BudgetEntryDTO budgetEntryDTO) {
        return budgetEntryService.createBudgetEntry(budgetId, budgetEntryDTO);
    }

    // BudgetEntry eines Budgets bearbeiten
    @PutMapping("/changebudgetentry/{budgetId}/{budgetEntryId}")
    public ExposeEntry changeBudgetEntry(@PathVariable Long budgetId, @PathVariable Long budgetEntryId, @RequestBody BudgetEntryDTO budgetEntryDTO) {
        return budgetEntryService.updateBudgetEntry(budgetId, budgetEntryId, budgetEntryDTO);
    }

    // BudgetEntry eines Budgets bearbeiten
    @PutMapping("/changeplanedamount/{budgetId}/{budgetEntryId}")
    public ExposeEntry changePlanedAmount(@PathVariable Long budgetId, @PathVariable Long budgetEntryId, @RequestBody Double planedAmount) {
        return budgetEntryService.updatePlanedAmount(budgetId, budgetEntryId, planedAmount);
    }

    // BudgetEntry eines Budgets bearbeiten
    @PutMapping("/updateusedamount/{budgetId}/{budgetEntryId}")
    public ExposeEntry addUsedAmount(@PathVariable Long budgetId, @PathVariable Long budgetEntryId, @RequestBody Double usedAmount) {
        return budgetEntryService.addUsedAmount(budgetId, budgetEntryId, usedAmount);
    }

    @PutMapping("/changeusedamount/{budgetId}/{budgetEntryId}")
    public ExposeEntry changeUsedAmount(@PathVariable Long budgetId, @PathVariable Long budgetEntryId, @RequestBody Double usedAmount) {
        return budgetEntryService.changeUsedAmount(budgetId, budgetEntryId, usedAmount);
    }

    // Typ eines BudgetEntrys bearbeiten
    @PutMapping("/changeTyp/{budgetId}/{budgetEntryId}")
    public ExposeEntry changeType(@PathVariable Long budgetId, @PathVariable Long budgetEntryId, @RequestBody String newTyp) {
        return budgetEntryService.changeType(budgetId, budgetEntryId, newTyp);
    }

     // Name eines BudgetEntrys bearbeiten
     @PutMapping("/changeName/{budgetId}/{budgetEntryId}")
     public ExposeEntry changeName(@PathVariable Long budgetId, @PathVariable Long budgetEntryId, @RequestBody String newName) {
         return budgetEntryService.changeName(budgetId, budgetEntryId, newName);
     }

      // description eines BudgetEntrys bearbeiten
    @PutMapping("/changeDescription/{budgetId}/{budgetEntryId}")
    public ExposeEntry changeDescription(@PathVariable Long budgetId, @PathVariable Long budgetEntryId, @RequestBody String newDes) {
        return budgetEntryService.changeDescription(budgetId, budgetEntryId, newDes);
    }

    // BudgetEntry eines Budgets löschen
    @DeleteMapping("/{budgetId}/{budgetEntryId}")
    public void deleteEntry(@PathVariable Long budgetId,@PathVariable Long budgetEntryId){
         budgetEntryService.deleteEntry(budgetId, budgetEntryId);
    }
}
