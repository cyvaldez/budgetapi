package com.budget.budgetapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(DotenvTestConfig.class)
class BudgetapiApplicationTests {

	@Test
	void contextLoads() {
		
	}
   
}
