package com.budget.budgetapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class BudgetapiApplication {

	public static void main(String[] args) {
         // Load the .env file
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASS", dotenv.get("DB_PASS"));
		System.setProperty("HASH_KEY", dotenv.get("HASH_KEY"));

        System.out.println("DB_USER = " + System.getProperty("DB_USER"));
		System.out.println("DB_PASS = " + System.getProperty("DB_PASS"));

		SpringApplication.run(BudgetapiApplication.class, args);
	}

}
