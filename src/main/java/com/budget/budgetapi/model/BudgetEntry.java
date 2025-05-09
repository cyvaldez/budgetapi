package com.budget.budgetapi.model;

import com.budget.budgetapi.dtos.budgetEntryDTO.BudgetEntryDTO;
import com.budget.budgetapi.dtos.budgetEntryDTO.TypeBudgetEntry;
import static com.budget.budgetapi.dtos.budgetEntryDTO.TypeBudgetEntry.stringToType;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name ="budget_entries")
public class BudgetEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private TypeBudgetEntry type;

    private Double planedAmount;
    private Double usedAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable=false)
    private Budget budget;

    public BudgetEntry(BudgetEntryDTO entryDTO){
        this.name=entryDTO.getName();
        this.description=entryDTO.getDescription();
        this.type= stringToType(entryDTO.getType());
        this.planedAmount=(double) entryDTO.getPlanedAmount();
        this.usedAmount=(double) entryDTO.getUsedAmount();
    }

    public BudgetEntry(){}

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

    public TypeBudgetEntry getType() {
        return type;
    }

    public void setType(TypeBudgetEntry type) {
        this.type = type;
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

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }
    
}

