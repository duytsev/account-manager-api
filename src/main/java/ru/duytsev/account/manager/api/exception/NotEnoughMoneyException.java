package ru.duytsev.account.manager.api.exception;

public class NotEnoughMoneyException extends RuntimeException {

	public NotEnoughMoneyException(Long accountId) {
		super(String.format("There is not enough money to withdraw from account: %s", accountId));
	}
}
