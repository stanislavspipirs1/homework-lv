package io.fourfinanceit.repository;

import io.fourfinanceit.domain.Loan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long> {

    Loan findOneByIdAndUsername(long id, String username);

    List<Loan> findByUsernameOrderByCreatedAtAsc(String username);
}