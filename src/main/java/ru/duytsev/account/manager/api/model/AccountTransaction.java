package ru.duytsev.account.manager.api.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
public class AccountTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "from_account_id")
	private Account fromAccount;
	@ManyToOne
	@JoinColumn(name = "to_account_id")
	private Account toAccount;
	@NotNull
	private BigDecimal amount;

	public AccountTransaction(Account fromAccount, Account toAccount, BigDecimal amount) {
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
	}
}
