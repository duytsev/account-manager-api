package ru.duytsev.account.manager.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.duytsev.account.manager.api.dto.AccountTransactionTO;
import ru.duytsev.account.manager.api.exception.NotEnoughMoneyException;
import ru.duytsev.account.manager.api.exception.ResourceNotFoundException;
import ru.duytsev.account.manager.api.model.Account;
import ru.duytsev.account.manager.api.model.AccountTransaction;
import ru.duytsev.account.manager.api.repository.AccountRepository;
import ru.duytsev.account.manager.api.repository.AccountTransactionRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class AccountService {

	private final AccountRepository accRepository;
	private final AccountTransactionRepository atRepository;

	@Autowired
	public AccountService(AccountRepository accRepository, AccountTransactionRepository atRepository) {
		this.accRepository = accRepository;
		this.atRepository = atRepository;
	}

	public Account saveAccount(Account account) {
		return accRepository.saveAndFlush(account);
	}

	public Account getAccountById(Long id) {
		Account acc = accRepository.findOne(id);
		checkAccount(id, acc);
		return acc;
	}

	public void deleteAccountById(Long id) {
		Account acc = accRepository.findOne(id);
		checkAccount(id, acc);
		accRepository.delete(acc);
	}

	public List<Account> getAllAccounts() {
		return accRepository.findAll();
	}

	public List<AccountTransaction> getAllTransactions() {
		return atRepository.findAll();
	}

	public AccountTransaction transfer(AccountTransactionTO tx) {
		Account accFrom = accRepository.findOneAndLock(tx.getFromAccountId());
		checkAccount(tx.getFromAccountId(), accFrom);
		Account accTo = accRepository.findOneAndLock(tx.getToAccountId());
		checkAccount(tx.getToAccountId(), accTo);
		// check if enough money on src account
		BigDecimal accFromNewBalance = accFrom.getBalance().subtract(tx.getAmount());
		if (accFromNewBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new NotEnoughMoneyException(accFrom.getId());
		}
		// calculate balance on dst account
		BigDecimal accToNewBalance = accTo.getBalance().add(tx.getAmount());
		// update both acc balances
		accFrom.setBalance(accFromNewBalance);
		accRepository.saveAndFlush(accFrom);
		accTo.setBalance(accToNewBalance);
		accRepository.saveAndFlush(accTo);
		// save account transaction
		AccountTransaction txEntity = new AccountTransaction(accFrom, accTo, tx.getAmount());
		return atRepository.saveAndFlush(txEntity);
	}

	public AccountTransaction changeBalance(Long accountId, BigDecimal delta) {
		Account account = accRepository.findOneAndLock(accountId);
		checkAccount(accountId, account);
		// check if enough money after withdraw
		BigDecimal newBalance = account.getBalance().add(delta);
		if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new NotEnoughMoneyException(account.getId());
		}
		// update account balance
		account.setBalance(newBalance);
		accRepository.saveAndFlush(account);

		// saveAccount transaction
		AccountTransaction tx = new AccountTransaction();
		tx.setAmount(delta.abs());
		if (delta.compareTo(BigDecimal.ZERO) > 0) {
			tx.setToAccount(account);
		} else {
			tx.setFromAccount(account);
		}
		return atRepository.saveAndFlush(tx);
	}

	private void checkAccount(Long id, Account acc) {
		if (acc == null) {
			throw new ResourceNotFoundException(id);
		}
	}
}
