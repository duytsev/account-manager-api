package ru.duytsev.account.manager.api.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.duytsev.account.manager.api.dto.AccountTO;
import ru.duytsev.account.manager.api.model.Account;
import ru.duytsev.account.manager.api.service.AccountService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

	private final AccountService accService;
	private final ModelMapper mapper;

	@Autowired
	public AccountController(AccountService accService, ModelMapper mapper) {
		this.accService = accService;
		this.mapper = mapper;
	}

	@RequestMapping(path = "/accounts/{accountId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAccount(@PathVariable("accountId") Long accountId) {
		Account account = accService.getAccountById(accountId);
		return ResponseEntity.ok().body(mapper.map(account, AccountTO.class));
	}

	@RequestMapping(path = "/accounts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAccount(@RequestBody @Valid AccountTO account, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationException(bindingResult.getAllErrors().toString());
		}
		Account created = accService.saveAccount(mapper.map(account, Account.class));
		return new ResponseEntity<>(mapper.map(created, AccountTO.class), HttpStatus.CREATED);
	}

	@RequestMapping(path = "/accounts", method = RequestMethod.GET)
	public ResponseEntity<?> list() {
		List<AccountTO> accTOs = accService.getAllAccounts().stream().
				map(tx -> mapper.map(tx, AccountTO.class)).collect(Collectors.toList());
		return ResponseEntity.ok().body(accTOs);
	}
}
