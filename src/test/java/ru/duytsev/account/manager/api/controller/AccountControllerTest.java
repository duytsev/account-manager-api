package ru.duytsev.account.manager.api.controller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.duytsev.account.manager.api.model.Account;
import ru.duytsev.account.manager.api.service.AccountService;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AccountControllerTest extends BaseControllerTest {

	private static boolean testDataCreated = false;

	@Autowired
	private AccountService accountService;

	@Override
	@Before
	public void setup() throws Exception {
		super.setup();

		// create 3 test accounts
		if (!testDataCreated) {
			accountService.saveAccount(createAccount(new BigDecimal(0)));
			accountService.saveAccount(createAccount(new BigDecimal(300)));
			accountService.saveAccount(createAccount(new BigDecimal(1000.1234)));
			testDataCreated = true;
		}
	}

	@Test
	public void testNewAccount() throws Exception {
		Account account = createAccount(new BigDecimal(100.100));
		mockMvc.perform(post("/api/accounts")
				.content(objectMapper.writeValueAsBytes(account))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(content().json("{'id': 4, 'balance': 100.100}"));
	}

	@Test
	public void testNewAccountWithInvalidBalance() throws Exception {
		Account account = createAccount(new BigDecimal(-1.11));
		mockMvc.perform(post("/api/accounts")
				.content(objectMapper.writeValueAsBytes(account))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorCode", is(1)))
				.andExpect(jsonPath("$.errorMessage", Matchers.containsString("rejected value")));
	}

	@Test
	public void testGetById() throws Exception {
		mockMvc.perform(get("/api/account/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content()
				.json("{'id': 1, 'balance': 0}"));
	}

	@Test
	@Ignore
	public void testList() throws Exception {
		mockMvc.perform(get("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content()
				.json("[{'id': 1, 'balance': 0},{'id': 2, 'balance': 300},{'id': 3, 'balance': 1000.1234}]"));
	}
}
