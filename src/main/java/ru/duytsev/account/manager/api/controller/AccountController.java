package ru.duytsev.account.manager.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.duytsev.account.manager.api.model.Account;
import ru.duytsev.account.manager.api.service.AccountService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

	private final AccountService accService;

	@Autowired
	public AccountController(AccountService accService) {
		this.accService = accService;
	}

	@RequestMapping(path = "/account/{accountId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAccount(@PathVariable("accountId") Long accountId) {
		Account account = accService.getAccountById(accountId);
		return ResponseEntity.ok().body(account);
	}

	@RequestMapping(path = "/accounts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAccount(@RequestBody @Valid Account account, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationException(bindingResult.getAllErrors().toString());
		}
		Account created = accService.saveAccount(account);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@RequestMapping(path = "/accounts", method = RequestMethod.GET)
	public ResponseEntity<List<Account>> list() {
		return ResponseEntity.ok().body(accService.getAllAccounts());
	}

	@RequestMapping(path = "/account/{accountId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAccount(@PathVariable("accountId") Long accountId) {
		accService.deleteAccountById(accountId);
		return ResponseEntity.noContent().build();
	}
}
