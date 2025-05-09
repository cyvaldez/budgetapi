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
            case INCOME -> user.setGlobalIncome(user.getGlobalIncome()+budgetEntry.getPlanedAmount());
            case INVESTMENT -> user.setGlobalInvestment(user.getGlobalInvestment()+budgetEntry.getPlanedAmount());
            case SAVING -> user.setGlobalSavings(user.getGlobalSavings()+budgetEntry.getPlanedAmount());
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
        var budgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry nicht gefunden"));
        if (!budgetEntry.getBudget().getId().equals(budgetId)) {
            throw new RuntimeException("Zugriffsfehler: Entry gehört nicht diesem Budget");
        }
        Budget budget= budgetEntry.getBudget();
        User user= budget.getUser();
        if(budgetEntry.getType()!= stringToType(budgetEntryDTO.getType().toUpperCase())){
            changeType(budgetId, budgetEntryId, budgetEntryDTO.getType());
        }
        switch(budgetEntry.getType()){
            case INCOME -> user.setGlobalIncome(user.getGlobalIncome()+budgetEntry.getPlanedAmount()+budgetEntryDTO.getPlanedAmount());
            case INVESTMENT -> user.setGlobalInvestment(user.getGlobalInvestment()+budgetEntry.getPlanedAmount()+budgetEntryDTO.getPlanedAmount());
            case SAVING -> user.setGlobalSavings(user.getGlobalSavings()+budgetEntry.getPlanedAmount()+budgetEntryDTO.getPlanedAmount());
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
        var budgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry nicht gefunden"));
        if (!budgetEntry.getBudget().getId().equals(budgetId)) {
            throw new RuntimeException("Zugriffsfehler: Entry gehört nicht diesem Budget");
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
        budget.setUsedAmount(budget.getUsedAmount()+ usedAmount);
        budgetRepository.save(budget);
        budgetEntry.setUsedAmount(budgetEntry.getUsedAmount()+usedAmount);
        return new ExposeEntry(budgetEntryRepository.save(budgetEntry));
    }

    //Ausgabe ändern
    public ExposeEntry changeUsedAmount(Long budgetId, Long budgetEntryId,Double usedAmount){
        var budgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry nicht gefunden"));
        if (!budgetEntry.getBudget().getId().equals(budgetId)) {
            throw new RuntimeException("Zugriffsfehler: Entry gehört nicht diesem Budget");
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
        var budgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry nicht gefunden"));
        if (!budgetEntry.getBudget().getId().equals(budgetId)) {
            throw new RuntimeException("Zugriffsfehler: Entry gehört nicht diesem Budget");
        }
        Budget budget= budgetEntry.getBudget();
        User user= budget.getUser();
        switch(budgetEntry.getType()){
            case INCOME -> user.setGlobalIncome(user.getGlobalIncome()+budgetEntry.getPlanedAmount()+planedAmount);
            case INVESTMENT -> user.setGlobalInvestment(user.getGlobalInvestment()+budgetEntry.getPlanedAmount()+planedAmount);
            case SAVING -> user.setGlobalSavings(user.getGlobalSavings()+budgetEntry.getPlanedAmount()+planedAmount);
            case SPENDING -> {
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
        var existingBudgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry nicht gefunden"));
        existingBudgetEntry.setDescription(newDes);
        return new ExposeEntry(budgetEntryRepository.save(existingBudgetEntry));
    }

    public ExposeEntry changeName(Long budgetId, Long budgetEntryId, String newName){
        var existingBudgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry nicht gefunden"));
        existingBudgetEntry.setName(newName);
        return new ExposeEntry(budgetEntryRepository.save(existingBudgetEntry));
    }

    public ExposeEntry changeType(Long budgetId, Long budgetEntryId, String newTyp){
        var budgetEntry = budgetEntryRepository.findById(budgetEntryId).orElseThrow(() -> new RuntimeException("BudgetEntry nicht gefunden"));
        Budget budget= budgetEntry.getBudget();
        User user= budget.getUser();
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
        }//between the two we have to update the database just to be sure 
        budgetRepository.save(budget);
        userRepository.save(user);
        switch(stringToType(newTyp.toUpperCase())){
            case INCOME -> user.setGlobalIncome(user.getGlobalIncome()+budgetEntry.getPlanedAmount());
            case INVESTMENT -> user.setGlobalInvestment(user.getGlobalInvestment()+budgetEntry.getPlanedAmount());
            case SAVING -> user.setGlobalSavings(user.getGlobalSavings()+budgetEntry.getPlanedAmount());
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
        var budget= budgetRepository.findById(budgetId).orElseThrow(() -> new RuntimeException("Budget nicht gefunden"));
        var budgetEntry= budgetEntryRepository.findById(entryId).orElseThrow(() -> new RuntimeException("BudgetEntry nicht gefunden"));
        if (!budgetEntry.getBudget().getId().equals(budgetId)) {
            throw new RuntimeException("Zugriffsfehler: Entry gehört nicht diesem Budget");
        }
        User user= budget.getUser();
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
        budgetRepository.save(budget);
        userRepository.save(user);

        budgetEntryRepository.deleteById(entryId);
    }
}
