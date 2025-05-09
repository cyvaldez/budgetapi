package com.budget.budgetapi.dtos.budgetEntryDTO;

import com.budget.budgetapi.model.BudgetEntry;

public class ExposeEntry {
    private Long id;
    private String name;
    private String description;
    private Long budgetId;
    private Double planedAmount;
    private Double usedAmount;
    private TypeBudgetEntry type;

    public ExposeEntry(BudgetEntry entry){
        this.id= entry.getId();
        this.name= entry.getName();
        this.description= entry.getDescription();
        this.budgetId=entry.getBudget().getId();
        this.planedAmount= entry.getPlanedAmount();
        this.usedAmount= entry.getUsedAmount();
        this.type= entry.getType();
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

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
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

    public TypeBudgetEntry getType() {
        return type;
    }

    public void setType(TypeBudgetEntry type) {
        this.type = type;
    }

}
