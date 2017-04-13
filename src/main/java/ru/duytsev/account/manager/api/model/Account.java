package ru.duytsev.account.manager.api.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private BigDecimal balance;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fromAccount")
	private Set<AccountTransaction> withdrawalTransactions;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "toAccount")
	private Set<AccountTransaction> depositTransactions;
}
