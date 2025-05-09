package com.budget.budgetapi.dtos.budgetDTO;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import com.budget.budgetapi.dtos.budgetEntryDTO.ExposeEntry;
import com.budget.budgetapi.model.Budget;
import com.budget.budgetapi.model.BudgetEntry;

public class ExposeBudget {
    private Long id;
    private String name;
    private String description;
    private Double planedAmount;
    private Double usedAmount;
    private Month month;
    private Long userId;
    private LocalDate creationDate;
    private LocalDate lastModifiedDate;
    private List<ExposeEntry> entries=new ArrayList<>();
    public ExposeBudget(){}

    public ExposeBudget(Budget budget){
        this.id= budget.getId();
        this.name= budget.getName();
        this.description= budget.getDescription();
        this.planedAmount= budget.getPlanedAmount();
        this.usedAmount= budget.getUsedAmount();
        this.month= budget.getMonth();
        this.userId= budget.getUser().getId();
        this.creationDate= budget.getCreationDate();
        this.lastModifiedDate= budget.getLastModifiedDate();
        for(BudgetEntry entry: budget.getBudgetentries()){
            entries.add(new ExposeEntry(entry));
        }   
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPlanedAmount() {
        return planedAmount;
    }

    public void setPlanedAmount(Double planedAmount) {
        this.planedAmount = planedAmount;
    }

    public Double getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(Double usedAmount) {
        this.usedAmount = usedAmount;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<ExposeEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ExposeEntry> entries) {
        this.entries = entries;
    }
    
}
