package ru.duytsev.account.manager.api.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AccountTO {

	private Long id;
	@NotNull
	@Min(0)
	private BigDecimal balance;
}
