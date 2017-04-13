package ru.duytsev.account.manager.api.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.duytsev.account.manager.api.dto.AccountBalanceChangeTO;
import ru.duytsev.account.manager.api.dto.AccountTransactionTO;
import ru.duytsev.account.manager.api.model.AccountTransaction;
import ru.duytsev.account.manager.api.service.AccountService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountTransactionController {

	private final AccountService accService;
	private final ModelMapper mapper;

	@Autowired
	public AccountTransactionController(AccountService accService, ModelMapper mapper) {
		this.accService = accService;
		this.mapper = mapper;
	}

	@RequestMapping(path = "/transfers", method = RequestMethod.GET)
	public ResponseEntity<?> list() {
		List<AccountTransactionTO> txTOs = accService.getAllTransactions().stream().
				map(tx -> mapper.map(tx, AccountTransactionTO.class)).collect(Collectors.toList());
		return ResponseEntity.ok().body(txTOs);
	}

	@RequestMapping(path = "/transfers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> transfer(@RequestBody @Valid AccountTransactionTO tx, BindingResult bindingResult) {
		validateTx(tx.getAmount(), bindingResult);
		AccountTransaction created = accService.transfer(tx);
		return new ResponseEntity<>(mapper.map(created, AccountTransactionTO.class), HttpStatus.CREATED);
	}

	@RequestMapping(path = "/deposits", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deposit(@RequestBody AccountBalanceChangeTO change, BindingResult bindingResult) {
		validateTx(change.getAmount(), bindingResult);
		AccountTransaction created = accService.changeBalance(change.getAccountId(), change.getAmount());
		return new ResponseEntity<>(mapper.map(created, AccountTransactionTO.class), HttpStatus.CREATED);
	}

	@RequestMapping(path = "/withdrawals", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> withdrawal(@RequestBody AccountBalanceChangeTO change, BindingResult bindingResult) {
		validateTx(change.getAmount(), bindingResult);
		AccountTransaction created = accService.changeBalance(change.getAccountId(), change.getAmount().negate());
		return new ResponseEntity<>(mapper.map(created, AccountTransactionTO.class), HttpStatus.CREATED);
	}

	private void validateTx(BigDecimal amount, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationException(bindingResult.getAllErrors().toString());
		}
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ValidationException("Amount has to be positive number");
		}
	}
}
