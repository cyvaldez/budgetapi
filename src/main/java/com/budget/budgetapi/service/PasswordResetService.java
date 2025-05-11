package com.budget.budgetapi.service;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.budget.budgetapi.model.PasswordResetToken;
import com.budget.budgetapi.model.User;
import com.budget.budgetapi.repository.PasswordResetTokenRepository;
import com.budget.budgetapi.repository.UserRepository;
import com.budget.budgetapi.security.JwtUtil;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl; // e.g. http://localhost:8080/reset-password
    private final JwtUtil jwtUtil;

    public PasswordResetService(JwtUtil jwtUtil){
        this.jwtUtil=jwtUtil;
    }

    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generatePasswordResetToken(email);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpirationDate(LocalDateTime.now().plusHours(1));

        tokenRepository.save(resetToken);
        String link = resetPasswordUrl + "?token=" + token;
        sendResetEmail(user.getEmail(), link);
    }

    private void sendResetEmail(String to, String link) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject("Password Reset Request");
        mail.setText("Click the link below to reset your password:\n \n" + link+"\n \n **Please note that this link will expired within 1 hour");
        mailSender.send(mail);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken); // Supprime le token après usage
    }
}
