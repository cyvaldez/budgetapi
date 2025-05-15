package com.budget.budgetapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.budget.budgetapi.dtos.budgetEntryDTO.BudgetEntryDTO;
import com.budget.budgetapi.dtos.budgetEntryDTO.ExposeEntry;
import static com.budget.budgetapi.dtos.budgetEntryDTO.TypeBudgetEntry.stringToType;
import com.budget.budgetapi.model.Budget;
import com.budget.budgetapi.model.BudgetEntry;
import com.budget.budgetapi.model.User;
import com.budget.budgetapi.repository.BudgetEntryRepository;
import com.budget.budgetapi.repository.BudgetRepository;
import com.budget.budgetapi.repository.UserRepository;
@Service
public class BudgetEntryService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetEntryRepository budgetEntryRepository;
 
    // show Budget of a defined user
    public List<ExposeEntry> getBudgetEntrysForBudget(Long budgetId) {
        List<ExposeEntry> entries= new ArrayList<>();
        for(BudgetEntry entry: budgetEntryRepository.findByBudgetId(budgetId)){
            entries.add(new ExposeEntry(entry));
        }
        return entries;
    }
    
    public ExposeEntry createBudgetEntry(Long budgetId, BudgetEntryDTO entryDTO){
        var budget = budgetRepository.findById(budgetId).orElseThrow(() -> new RuntimeException("Budger not found"));
        var budgetEntry = new BudgetEntry(entryDTO);
        //Planed Amount will will be update
        User user= budget.getUser();
        switch(budgetEntry.getType()){
            case INCOME -> {
                budget.setIncomeThisMonth(budget.getIncomeThisMonth()+budgetEntry.getPlanedAmount());
                user.setGlobalIncome(user.getGlobalIncome()+budgetEntry.getPlanedAmount());
            }
            case INVESTMENT -> {
                budget.setInvested(budget.getInvested()+ budgetEntry.getPlanedAmount());
                user.setGlobalInvestment(user.getGlobalInvestment()+budgetEntry.getPlanedAmount());
            }
            case SAVING -> {
                budget.setSavings(budget.getSavings()+budgetEntry.getPlanedAmount());
                user.setGlobalSavings(user.getGlobalSavings()+budgetEntry.getPlanedAmount());
            }
            case SPENDING -> {
                user.setGlobalSpending(user.getGlobalSpending()+budgetEntry.getUsedAmount());
                budget.setPlanedAmount(budget.getPlanedAmount()+budgetEntry.getPlanedAmount());
                budget.setUsedAmount(budget.getUsedAmount()+budgetEntry.getUsedAmount());
            }
            default -> throw new IllegalArgumentException("Invalid Type");
        }
        budgetRepository.save(budget);
        userRepository.save(user);
        budgetEntry.setBudget(budget);
        return new ExposeEntry (budgetEntryRepository.save(budgetEntry));
    }
    public ExposeEntry updateBudgetEntry(Long budgetId, Long budgetEntryId, BudgetEntryDTO budgetEntryDTO) {
        var budgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry not found"));
        if (!budgetEntry.getBudget().getId().equals(budgetId)) {
            throw new RuntimeException("Access failure: This entry dont belong to this Budget");
        }
        Budget budget= budgetEntry.getBudget();
        User user= budget.getUser();
        if(budgetEntry.getType()!= stringToType(budgetEntryDTO.getType().toUpperCase())){
            changeType(budgetId, budgetEntryId, budgetEntryDTO.getType());
        }
        switch(budgetEntry.getType()){
            case INCOME -> {
                budget.setIncomeThisMonth(budget.getIncomeThisMonth()-budgetEntry.getPlanedAmount()+budgetEntryDTO.getPlanedAmount());
                user.setGlobalIncome(user.getGlobalIncome()-budgetEntry.getPlanedAmount()+budgetEntryDTO.getPlanedAmount());
            }
            case INVESTMENT -> {
                budget.setInvested(budget.getInvested()- budgetEntry.getPlanedAmount()+budgetEntryDTO.getPlanedAmount());
                user.setGlobalInvestment(user.getGlobalInvestment()-budgetEntry.getPlanedAmount()+budgetEntryDTO.getPlanedAmount());
            }
            case SAVING -> {
                budget.setSavings(budget.getSavings()-budgetEntry.getPlanedAmount()+budgetEntryDTO.getPlanedAmount());
                user.setGlobalSavings(user.getGlobalSavings()-budgetEntry.getPlanedAmount()+budgetEntryDTO.getPlanedAmount());
            }
            case SPENDING -> {
                user.setGlobalSpending(user.getGlobalSpending()-budgetEntry.getUsedAmount()+budgetEntryDTO.getUsedAmount());
                budget.setPlanedAmount(budget.getPlanedAmount()-budgetEntry.getPlanedAmount()+budgetEntryDTO.getPlanedAmount());
                budget.setUsedAmount(budget.getUsedAmount()-budgetEntry.getUsedAmount()+budgetEntryDTO.getUsedAmount());
            }
            default -> throw new IllegalArgumentException("Invalid Type");
        }

        budgetEntry.setName(budgetEntryDTO.getName());
        budgetEntry.setPlanedAmount(budgetEntryDTO.getPlanedAmount());
        budgetEntry.setUsedAmount(budgetEntryDTO.getUsedAmount());
        budgetEntry.setDescription(budgetEntryDTO.getDescription());

        budgetRepository.save(budget);
        userRepository.save(user);
        return new ExposeEntry(budgetEntryRepository.save(budgetEntry));
    }

    //Ausgabe buchen
    public ExposeEntry addUsedAmount(Long budgetId, Long budgetEntryId,Double usedAmount){
        var budgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry not found"));
        if (!budgetEntry.getBudget().getId().equals(budgetId)) {
            throw new RuntimeException("Access failure: This entry dont belong to this Budget");
        }
        Budget budget= budgetEntry.getBudget();
        User user= budget.getUser();
        switch(budgetEntry.getType()){
            case SPENDING -> {
                user.setGlobalSpending(user.getGlobalSpending()+usedAmount);
                budget.setUsedAmount(budget.getUsedAmount()+usedAmount);
                budgetRepository.save(budget);
                userRepository.save(user);
            }
            default -> throw new IllegalArgumentException("Invalid Type");
        }
        budgetEntry.setUsedAmount(budgetEntry.getUsedAmount()+usedAmount);
        return new ExposeEntry(budgetEntryRepository.save(budgetEntry));
    }

    //Ausgabe ändern
    public ExposeEntry changeUsedAmount(Long budgetId, Long budgetEntryId,Double usedAmount){
        var budgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry not found"));
        if (!budgetEntry.getBudget().getId().equals(budgetId)) {
            throw new RuntimeException("Access failure: This entry dont belong to this Budget");
        }
        Budget budget= budgetEntry.getBudget();
        User user= budget.getUser();
        switch(budgetEntry.getType()){
            case SPENDING -> {
                user.setGlobalSpending(user.getGlobalSpending()-budgetEntry.getUsedAmount()+usedAmount);
                budget.setUsedAmount(budget.getUsedAmount()-budgetEntry.getUsedAmount()+usedAmount);
                
            }
            default -> throw new IllegalArgumentException("Invalid Type");
        }
        budgetRepository.save(budget);
        userRepository.save(user);
        budgetEntry.setUsedAmount(usedAmount);
        return new ExposeEntry( budgetEntryRepository.save(budgetEntry));
    }

    //modify the planedAmount for an entry
    public ExposeEntry updatePlanedAmount(Long budgetId, Long budgetEntryId,Double planedAmount){
        var budgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry not found"));
        if (!budgetEntry.getBudget().getId().equals(budgetId)) {
            throw new RuntimeException("Access failure: This entry dont belong to this Budget");
        }
        Budget budget= budgetEntry.getBudget();
        User user= budget.getUser();
        switch(budgetEntry.getType()){
            case INCOME -> {
                budget.setIncomeThisMonth(budget.getIncomeThisMonth()-budgetEntry.getPlanedAmount()+planedAmount);
                user.setGlobalIncome(user.getGlobalIncome()-budgetEntry.getPlanedAmount()+planedAmount);
            }
            case INVESTMENT -> {
                budget.setInvested(budget.getInvested()- budgetEntry.getPlanedAmount()+planedAmount);
                user.setGlobalInvestment(user.getGlobalInvestment()-budgetEntry.getPlanedAmount()+planedAmount);
            }
            case SAVING -> {
                budget.setSavings(budget.getSavings()-budgetEntry.getPlanedAmount()+planedAmount);
                user.setGlobalSavings(user.getGlobalSavings()-budgetEntry.getPlanedAmount()+planedAmount);
            }
            case SPENDING -> {
                user.setGlobalSpending(user.getGlobalSpending()-budgetEntry.getUsedAmount()+planedAmount);
                budget.setPlanedAmount(budget.getPlanedAmount()-budgetEntry.getPlanedAmount()+planedAmount);
            }
            default -> throw new IllegalArgumentException("Invalid Type");
        }
        budgetRepository.save(budget);
        userRepository.save(user);
        budgetEntry.setPlanedAmount(planedAmount);
        return new ExposeEntry(budgetEntryRepository.save(budgetEntry));
    }

    public ExposeEntry changeDescription(Long budgetId, Long budgetEntryId, String newDes){
        var existingBudgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry not found"));
        existingBudgetEntry.setDescription(newDes);
        return new ExposeEntry(budgetEntryRepository.save(existingBudgetEntry));
    }

    public ExposeEntry changeName(Long budgetId, Long budgetEntryId, String newName){
        var existingBudgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry not found"));
        existingBudgetEntry.setName(newName);
        return new ExposeEntry(budgetEntryRepository.save(existingBudgetEntry));
    }

    public ExposeEntry changeType(Long budgetId, Long budgetEntryId, String newTyp){
        var budgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry not found"));
        Budget budget= budgetEntry.getBudget();
        User user= budget.getUser();
        switch(budgetEntry.getType()){
            case INCOME -> {
                budget.setIncomeThisMonth(budget.getIncomeThisMonth()-budgetEntry.getPlanedAmount());
                user.setGlobalIncome(user.getGlobalIncome()-budgetEntry.getPlanedAmount());
            }
            case INVESTMENT -> {
                budget.setInvested(budget.getInvested()- budgetEntry.getPlanedAmount());
                user.setGlobalInvestment(user.getGlobalInvestment()-budgetEntry.getPlanedAmount());
            }
            case SAVING -> {
                budget.setSavings(budget.getSavings()-budgetEntry.getPlanedAmount());
                user.setGlobalSavings(user.getGlobalSavings()-budgetEntry.getPlanedAmount());
            }
            case SPENDING -> {
                user.setGlobalSpending(user.getGlobalSpending()-budgetEntry.getUsedAmount());
                budget.setPlanedAmount(budget.getPlanedAmount()-budgetEntry.getPlanedAmount());
                budget.setUsedAmount( budget.getUsedAmount()-budgetEntry.getUsedAmount());
            }
            default -> throw new IllegalArgumentException("Invalid Type");
        }
        //between the two we have to update the database just to be sure 
        budgetRepository.save(budget);
        userRepository.save(user);
        switch(stringToType(newTyp.toUpperCase())){
            case INCOME -> {
                budget.setIncomeThisMonth(budget.getIncomeThisMonth()+budgetEntry.getPlanedAmount());
                user.setGlobalIncome(user.getGlobalIncome()+budgetEntry.getPlanedAmount());
            }
            case INVESTMENT -> {
                budget.setInvested(budget.getInvested()+ budgetEntry.getPlanedAmount());
                user.setGlobalInvestment(user.getGlobalInvestment()+budgetEntry.getPlanedAmount());
            }
            case SAVING -> {
                budget.setSavings(budget.getSavings()+budgetEntry.getPlanedAmount());
                user.setGlobalSavings(user.getGlobalSavings()+budgetEntry.getPlanedAmount());
            }
            case SPENDING -> {
                user.setGlobalSpending(user.getGlobalSpending()+budgetEntry.getUsedAmount());
                budget.setPlanedAmount(budget.getPlanedAmount()+budgetEntry.getPlanedAmount());
                budget.setUsedAmount(budget.getUsedAmount()+budgetEntry.getUsedAmount());
            }
            default -> throw new IllegalArgumentException("Invalid Type");
        }
        budgetRepository.save(budget);
        userRepository.save(user);
        budgetEntry.setType(stringToType(newTyp.toUpperCase()));
        return new ExposeEntry(budgetEntryRepository.save(budgetEntry));
    }
    
    public void deleteEntry(Long budgetId,Long entryId) {
        if (!budgetEntryRepository.existsById(entryId)) {
            throw new RuntimeException("Entry not found with ID: " + entryId);
        }
        var budget= budgetRepository.findById(budgetId).orElseThrow(() -> new RuntimeException("Budget not found"));
        var budgetEntry= budgetEntryRepository.findById(entryId).orElseThrow(() -> new RuntimeException("BudgetEntry not found"));
        if (!budgetEntry.getBudget().getId().equals(budgetId)) {
            throw new RuntimeException("Access failure: This entry dont belong to this Budget");
        }
        User user= budget.getUser();
        switch(budgetEntry.getType()){
            case INCOME -> {
                budget.setIncomeThisMonth(budget.getIncomeThisMonth()-budgetEntry.getPlanedAmount());
                user.setGlobalIncome(user.getGlobalIncome()-budgetEntry.getPlanedAmount());
            }
            case INVESTMENT -> {
                budget.setInvested(budget.getInvested()- budgetEntry.getPlanedAmount());
                user.setGlobalInvestment(user.getGlobalInvestment()-budgetEntry.getPlanedAmount());
            }
            case SAVING -> {
                budget.setSavings(budget.getSavings()-budgetEntry.getPlanedAmount());
                user.setGlobalSavings(user.getGlobalSavings()-budgetEntry.getPlanedAmount());
            }
            case SPENDING -> {
                user.setGlobalSpending(user.getGlobalSpending()-budgetEntry.getUsedAmount());
                budget.setPlanedAmount(budget.getPlanedAmount()-budgetEntry.getPlanedAmount());
                budget.setUsedAmount(budget.getUsedAmount()-budgetEntry.getUsedAmount());
            }
            default -> throw new IllegalArgumentException("Invalid Type");
        }
        budgetRepository.save(budget);
        userRepository.save(user);

        budgetEntryRepository.deleteById(entryId);
    }
}
