package com.budget.budgetapi;

import org.springframework.boot.test.context.TestConfiguration;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

@TestConfiguration
public class DotenvTestConfig {

    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASS", dotenv.get("DB_PASS"));
        System.setProperty("HASH_KEY", dotenv.get("HASH_KEY"));

        System.out.println("=== VARIABLES .env CHARGÉES ===");
        System.out.println("DB_USER = " + System.getProperty("DB_USER"));
    }
}
