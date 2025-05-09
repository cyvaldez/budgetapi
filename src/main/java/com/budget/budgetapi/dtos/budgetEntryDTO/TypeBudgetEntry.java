package com.budget.budgetapi.dtos.budgetEntryDTO;

public enum TypeBudgetEntry {
    INCOME,
    SAVING,
    INVESTMENT,
    SPENDING;

    public static TypeBudgetEntry stringToType(String s){
        switch (s.toUpperCase()) {
            case "INCOME" -> { 
                return TypeBudgetEntry.INCOME;
            }
            case "SAVING" -> {
                return TypeBudgetEntry.SAVING;
            }
             case "INVESTMENT" -> {
                 return TypeBudgetEntry.INVESTMENT;
            }
             case "SPENDING" -> {
                 return TypeBudgetEntry.SPENDING;
            }
            default -> throw new AssertionError();
        }
    }
}

   

