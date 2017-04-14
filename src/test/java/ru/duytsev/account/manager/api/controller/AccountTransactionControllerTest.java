package ru.duytsev.account.manager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.duytsev.account.manager.api.dto.AccountBalanceChangeTO;
import ru.duytsev.account.manager.api.dto.AccountTransactionTO;
import ru.duytsev.account.manager.api.model.Account;
import ru.duytsev.account.manager.api.service.AccountService;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AccountTransactionControllerTest extends BaseControllerTest {

	private static boolean testDataCreated = false;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testDeposit() throws Exception {
		Account account = accountService.saveAccount(createAccount(new BigDecimal(3000)));

		AccountBalanceChangeTO change = new AccountBalanceChangeTO(account.getId(), new BigDecimal(150));
		mockMvc.perform(post("/api/deposits")
				.content(objectMapper.writeValueAsBytes(change))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.toAccountId", isLong(account.getId())))
				.andExpect(jsonPath("$.amount", is(150)));

		checkBalance(account, 3150.0);
	}

	@Test
	public void testWithdrawal() throws Exception {
		Account account = accountService.saveAccount(createAccount(new BigDecimal(100.100)));

		AccountBalanceChangeTO change = new AccountBalanceChangeTO(account.getId(), new BigDecimal(0.100));
		mockMvc.perform(post("/api/withdrawals")
				.content(objectMapper.writeValueAsBytes(change))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.fromAccountId", isLong(account.getId())))
				.andExpect(jsonPath("$.amount", is(closeTo(new BigDecimal(0.100), new BigDecimal(0.000001)))));

		checkBalance(account, 100.0);
	}

	@Test
	public void testWithdrawalNotEnoughMoney() throws Exception {
		Account account = accountService.saveAccount(createAccount(new BigDecimal(100)));

		AccountBalanceChangeTO change = new AccountBalanceChangeTO(account.getId(), new BigDecimal(1000));
		mockMvc.perform(post("/api/withdrawals")
				.content(objectMapper.writeValueAsBytes(change))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable())
				.andExpect(jsonPath("$.errorCode", is(2)))
				.andExpect(jsonPath("$.errorMessage",
						Matchers.containsString("There is not enough money to withdraw from account: " + account.getId())));

		checkBalance(account, 100.0);

	}

	@Test
	public void testTransfer() throws Exception {
		Account accFrom = accountService.saveAccount(createAccount(new BigDecimal(100)));
		Account accTo = accountService.saveAccount(createAccount(new BigDecimal(100)));

		AccountTransactionTO tx = new AccountTransactionTO();
		tx.setFromAccountId(accFrom.getId());
		tx.setToAccountId(accTo.getId());
		tx.setAmount(new BigDecimal(100));
		mockMvc.perform(post("/api/transfers")
				.content(objectMapper.writeValueAsBytes(tx))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.fromAccountId", isLong(accFrom.getId())))
				.andExpect(jsonPath("$.toAccountId", isLong(accTo.getId())))
				.andExpect(jsonPath("$.amount", is(100)));

		checkBalance(accFrom, 0.0);
		checkBalance(accTo, 200.0);
	}

	@Test
	public void testTransferNotEnoughMoney() throws Exception {
		Account accFrom = accountService.saveAccount(createAccount(new BigDecimal(100)));
		Account accTo = accountService.saveAccount(createAccount(new BigDecimal(100)));

		AccountTransactionTO tx = new AccountTransactionTO();
		tx.setFromAccountId(accFrom.getId());
		tx.setToAccountId(accTo.getId());
		tx.setAmount(new BigDecimal(1000));
		mockMvc.perform(post("/api/transfers")
				.content(objectMapper.writeValueAsBytes(tx))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable())
				.andExpect(jsonPath("$.errorCode", is(2)))
				.andExpect(jsonPath("$.errorMessage",
						Matchers.containsString("There is not enough money to withdraw from account: " + accFrom.getId())));

		checkBalance(accFrom, 100.0);
		checkBalance(accTo, 100.0);
	}

	private void checkBalance(Account account, Double balance) throws Exception {
		mockMvc.perform(get("/api/account/" + account.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", isLong(account.getId())))
				.andExpect(jsonPath("$.balance", is(balance)));
	}
}
