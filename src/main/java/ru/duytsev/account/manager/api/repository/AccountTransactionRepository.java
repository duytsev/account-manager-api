package ru.duytsev.account.manager.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.duytsev.account.manager.api.model.AccountTransaction;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {
}
