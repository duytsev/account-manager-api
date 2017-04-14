package ru.duytsev.account.manager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.duytsev.account.manager.api.model.Account;

import java.math.BigDecimal;

public abstract class BaseControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	protected ObjectMapper objectMapper;

	protected MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	protected Account createAccount(BigDecimal balance) {
		Account acc = new Account();
		acc.setBalance(balance);
		return acc;
	}

	// dirty hack
	public static Matcher<Integer> isLong(Long value) {
		return org.hamcrest.core.Is.is(value.intValue());
	}

}
