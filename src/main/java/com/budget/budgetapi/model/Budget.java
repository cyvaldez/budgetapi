package com.budget.budgetapi.model;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import com.budget.budgetapi.dtos.budgetDTO.BudgetDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double planedAmount;
    private Double usedAmount;
    private Double savings;
    private Double invested;
    private Double incomeThisMonth;
    private LocalDate creationDate;
    private LocalDate lastModifiedDate;
    
    private Month month =LocalDate.now().getMonth();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable=false)
    private User user;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<BudgetEntry> budgetentries = new ArrayList<>();

    public Budget(BudgetDTO budgetDTO){
        this.name= budgetDTO.getName();
        this.description= budgetDTO.getDescription();
        this.creationDate= LocalDate.now();
        this.lastModifiedDate=LocalDate.now();
        this.month=Month.valueOf(budgetDTO.getMonth().toUpperCase()) ;
        this.planedAmount=0.0;
        this.usedAmount=0.0;
        this.savings=0d;
        this.invested=0d;
        this.incomeThisMonth=0d;
    }
    public Budget(){}

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

    public Double getSavings() {
        return savings;
    }
    public void setSavings(Double savings) {
        this.savings = savings;
    }
    public Double getInvested() {
        return invested;
    }
    public void setInvested(Double invested) {
        this.invested = invested;
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

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<BudgetEntry> getBudgetentries() {
        return budgetentries;
    }

    public void setBudgetentries(List<BudgetEntry> budgetentries) {
        this.budgetentries = budgetentries;
    }
    public Double getIncomeThisMonth() {
        return incomeThisMonth;
    }
    public void setIncomeThisMonth(Double incomeThisMonth) {
        this.incomeThisMonth = incomeThisMonth;
    }

}