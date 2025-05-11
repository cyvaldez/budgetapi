package com.budget.budgetapi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.budget.budgetapi.service.PasswordResetService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestReset(@RequestBody Map<String, String> body) {
        try{
        passwordResetService.createPasswordResetToken(body.get("email"));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Request oppps failed: "+e.getMessage());
        }
        return ResponseEntity.ok(body.get("name")+" a reset link have been sent to you via Email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
       try{
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        passwordResetService.resetPassword(token, newPassword);
          
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: "+e.getMessage());
        }
            return ResponseEntity.ok("Password successfully reset");
    }
}
