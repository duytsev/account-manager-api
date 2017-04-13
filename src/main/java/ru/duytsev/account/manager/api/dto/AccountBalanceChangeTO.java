package ru.duytsev.account.manager.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AccountBalanceChangeTO {

	@NotNull
	private final Long accountId;
	@NotNull
	private final BigDecimal amount;
}

