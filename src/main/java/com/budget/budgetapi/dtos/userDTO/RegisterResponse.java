package com.budget.budgetapi.dtos.userDTO;

public class RegisterResponse {
   private ExposeUser exposeUser;
   private String token;

   public RegisterResponse(ExposeUser user, String token){
    this.exposeUser= user;
    this.token= token;
   }
   public ExposeUser getExposeUser() {
    return exposeUser;
   }
   public void setExposeUser(ExposeUser exposeUser) {
    this.exposeUser = exposeUser;
   }
   public String getToken() {
    return token;
   }
   public void setToken(String token) {
    this.token = token;
   }
}
