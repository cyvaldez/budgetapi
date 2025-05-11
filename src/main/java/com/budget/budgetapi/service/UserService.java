package com.budget.budgetapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.budget.budgetapi.dtos.UserAuth;
import com.budget.budgetapi.dtos.userDTO.ChangeEmailDTO;
import com.budget.budgetapi.dtos.userDTO.ChangeUserName;
import com.budget.budgetapi.dtos.userDTO.ExposeUser;
import com.budget.budgetapi.dtos.userDTO.RegisterDTO;
import com.budget.budgetapi.dtos.userDTO.changePasswordDTO;
import com.budget.budgetapi.model.User;
import com.budget.budgetapi.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ExposeUser registerUser(RegisterDTO registerDTO) {
        // Password encoded
        User user= new User();
        user.setEmail(registerDTO.getEmail());
        user.setName(registerDTO.getName());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        return new ExposeUser(userRepository.save(user));
    }

    public ExposeUser loginUser(String email, String rawPassword) throws IllegalAccessException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return new ExposeUser(user);
        } else {
            throw new IllegalAccessException("Wrong Password!");
        }
    }

    public ExposeUser getMe(UserAuth userAuth){
        User user = userRepository.findByEmail(userAuth.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        return new ExposeUser(user);
    }
    
    public ExposeUser changePassword(Long userId, changePasswordDTO change){
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(change.getAltPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(change.getNewPassword()));
            return new ExposeUser(userRepository.save(user));
        }
        throw new IllegalAccessError("wrong password");
    }

    public ExposeUser changeUserEmail(Long userId, ChangeEmailDTO change){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
            if (passwordEncoder.matches(change.getPassword(), user.getPassword())) {
                user.setEmail(change.getNewEmail());
                return new ExposeUser(userRepository.save(user));
            }
            throw new IllegalAccessError("wrong password");
    }

    public ExposeUser changeUserName(Long userId, ChangeUserName change){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
            if (passwordEncoder.matches(change.getPassword(), user.getPassword())) {
                user.setName(change.getNewName());
                return new ExposeUser(userRepository.save(user));
            }
            throw new IllegalAccessError("wrong password");
    }
/* 
    public ExposeUser updateGlobalvalues(Long userId){
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
        Double spending, invest, saving, income;
        spending=0d;
        invest=0.0;
        saving= 0d;
        income=0d;

        for(Budget budget: user.getBudgets()){
            for(BudgetEntry entry: budget.getBudgetentries()){
                switch(entry.getType()){
                    case INCOME:
                    income=+ entry.getPlanedAmount();
                    break;
                    case INVERSTMENT:
                    invest=+ entry.getPlanedAmount();
                    break;
                    case SAVING:
                    saving=+ entry.getPlanedAmount();
                    break;
                    case SPENDING:
                    spending=+ entry.getUsedAmount();
                    break;
                    default: 
                    throw new IllegalArgumentException("Invalid Type");
                }
            }
        }

        user.setGlobalIncome(income);
        user.setGlobalInvestment(invest);
        user.setGlobalSavings(saving);
        user.setGlobalSpending(spending);
        return new ExposeUser( userRepository.save(user));
    }
*/
    public void deleteUserAccount( Long userId){
         userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.deleteById(userId);
         
    }
    
}