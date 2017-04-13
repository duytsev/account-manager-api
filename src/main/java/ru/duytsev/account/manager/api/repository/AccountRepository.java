package ru.duytsev.account.manager.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.duytsev.account.manager.api.model.Account;

import javax.persistence.LockModeType;

public interface AccountRepository extends JpaRepository<Account, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select acc from Account acc where acc.id = :id")
	Account findOneAndLock(@Param("id") Long id);
}
