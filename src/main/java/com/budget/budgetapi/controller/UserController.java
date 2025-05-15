package com.budget.budgetapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.budget.budgetapi.dtos.UserAuth;
import com.budget.budgetapi.dtos.userDTO.ChangeEmailDTO;
import com.budget.budgetapi.dtos.userDTO.ChangeUserName;
import com.budget.budgetapi.dtos.userDTO.ExposeUser;
import com.budget.budgetapi.dtos.userDTO.LoginDTO;
import com.budget.budgetapi.dtos.userDTO.RegisterDTO;
import com.budget.budgetapi.dtos.userDTO.RegisterResponse;
import com.budget.budgetapi.dtos.userDTO.changePasswordDTO;
import com.budget.budgetapi.security.JwtUtil;
import com.budget.budgetapi.service.UserService;


@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO user) {
         String token = jwtUtil.generateToken(user.getEmail());
         ExposeUser exposeUser= userService.registerUser(user);
         RegisterResponse answer= new RegisterResponse(exposeUser, token);
        return ResponseEntity.ok(answer);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO user) {
        try {
            ExposeUser loggedInUser = userService.loginUser(user.getEmail(), user.getPassword());
            String token = jwtUtil.generateToken(loggedInUser.getEmail());
            return ResponseEntity.ok(token);
        } catch (RuntimeException | IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login fehlgeschlagen: " + e.getMessage());
        }
    }
    @PostMapping("/logout")
    public void logoutUser(@RequestBody String email){

        userService.logoutUser(email);
        
    }
    

    @GetMapping("user/me")
    public ExposeUser getMe(@AuthenticationPrincipal UserAuth user) {
        return userService.getMe(user);
    }
    

    @PutMapping("/{userId}/changemypassword")
    public ResponseEntity<?> changePassword(@PathVariable Long userId,@RequestBody changePasswordDTO change){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserAuth connectedUser = (UserAuth) auth.getPrincipal(); // fonctionne
        if(connectedUser.getId().equals(userId)){
          return ResponseEntity.ok( userService.changePassword(userId, change)) ;
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You aren't allowed to make this modification");
        //throw new IllegalAccessError("You aren't allowed to make this modification");
    }

    @PutMapping("/{userId}/changemyemail")
    public ResponseEntity<?> changeEmail(@PathVariable Long userId,@RequestBody ChangeEmailDTO change){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserAuth connectedUser = (UserAuth) auth.getPrincipal(); // fonctionne
        if(connectedUser.getId().equals(userId)){
          return ResponseEntity.ok( userService.changeUserEmail(userId, change)) ;
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You aren't allowed to make this modification");
    }

    @PutMapping("/{userId}/changemyusername")
    public ResponseEntity<?> changeUserName(@PathVariable Long userId,@RequestBody ChangeUserName change){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserAuth connectedUser = (UserAuth) auth.getPrincipal(); // fonctionne
        if(connectedUser.getId().equals(userId)){
          return ResponseEntity.ok( userService.changeUserName(userId, change));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You aren't allowed to make this modification");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserAccount(@PathVariable Long userId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserAuth connectedUser = (UserAuth) auth.getPrincipal(); // fonctionne
        if(connectedUser.getId().equals(userId)){
            userService.deleteUserAccount(userId);
              return ResponseEntity.ok("Good Bye "+ connectedUser.getName()+ "\nWe are sorry to see you leave!\nWe will be glad to see you again!");
        } else
        throw new org.springframework.security.access.AccessDeniedException("You aren't not allowed to make this modification");
    }
}