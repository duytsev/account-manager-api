package ru.duytsev.account.manager.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountTransactionTO {
	private Long id;
	private Long fromAccountId;
	private Long toAccountId;
	@NotNull
	private BigDecimal amount;
	private LocalDateTime createdDate;
}
