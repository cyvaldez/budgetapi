package com.budget.budgetapi.dtos.budgetEntryDTO;


public class BudgetEntryDTO {
    private String name;
    private String description;
    private String type;
    private Double planedAmount;
    private Double usedAmount;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
}
