package com.budget.budgetapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class BudgetapiApplication {

	public static void main(String[] args) {
         // Load the .env file
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        //set env variable
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASS", dotenv.get("DB_PASS"));
		System.setProperty("HASH_KEY", dotenv.get("HASH_KEY"));
		System.setProperty("MAIL_USER", dotenv.get("MAIL_USER"));
		System.setProperty("MAIL_PASS", dotenv.get("MAIL_PASS"));
		System.setProperty("DB_URL", dotenv.get("DB_URL"));

		SpringApplication.run(BudgetapiApplication.class, args);
	}

}
