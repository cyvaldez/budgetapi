package com.budget.budgetapi.dtos.userDTO;

public class changePasswordDTO {
 private String altPassword;
 private String newPassword;

    public String getAltPassword() {
        return altPassword;
    }

    public void setAltPassword(String altPassword) {
        this.altPassword = altPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
 
}
