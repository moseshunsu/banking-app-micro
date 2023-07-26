package bankingMicroservice.transactionservice.repository;

import bankingMicroservice.transactionservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
