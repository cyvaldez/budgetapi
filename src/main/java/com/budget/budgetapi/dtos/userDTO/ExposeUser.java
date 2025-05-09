package com.budget.budgetapi.dtos.userDTO;

import java.util.ArrayList;
import java.util.List;

import com.budget.budgetapi.dtos.budgetDTO.ExposeBudget;
import com.budget.budgetapi.model.Budget;
import com.budget.budgetapi.model.User;

public class ExposeUser {
    private Long id;

    private String email;
    private String name;
    private Double globalSpending;
    private Double globalIncome;
    private Double globalSavings;
    private Double globalInvestment;

    private List<ExposeBudget> budgets = new ArrayList<>();

    public ExposeUser(User user){
        this.id=user.getId();
        this.email= user.getEmail();
        this.name= user.getName();
        this.globalIncome=user.getGlobalIncome();
        this.globalInvestment= user.getGlobalInvestment();
        this.globalSavings= user.getGlobalSavings();
        this.globalSpending= user.getGlobalSpending();
        for(Budget budget: user.getBudgets()){
            this.budgets.add(new ExposeBudget(budget));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getGlobalSpending() {
        return globalSpending;
    }

    public void setGlobalSpending(Double globalSpending) {
        this.globalSpending = globalSpending;
    }

    public Double getGlobalIncome() {
        return globalIncome;
    }

    public void setGlobalIncome(Double globalIncome) {
        this.globalIncome = globalIncome;
    }

    public Double getGlobalSavings() {
        return globalSavings;
    }

    public void setGlobalSavings(Double globalSavings) {
        this.globalSavings = globalSavings;
    }

    public Double getGlobalInvestment() {
        return globalInvestment;
    }

    public void setGlobalInvestment(Double globalInvestment) {
        this.globalInvestment = globalInvestment;
    }

    public List<ExposeBudget> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<ExposeBudget> budgets) {
        this.budgets = budgets;
    }
    
}
