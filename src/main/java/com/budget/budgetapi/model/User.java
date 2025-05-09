package com.budget.budgetapi.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;
    private Double globalSpending=0d;
    private Double globalIncome=0d;
    private Double globalSavings=0d;
    private Double globalInvestment=0d;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Budget> budgets = new ArrayList<>();

    // Getter & Setter
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email= email;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name= name;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password= password;
    }

    public List<Budget> getBudgets(){
        return budgets;
    }
    public void setBudgets(List<Budget> budgets){
        this.budgets= budgets;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
