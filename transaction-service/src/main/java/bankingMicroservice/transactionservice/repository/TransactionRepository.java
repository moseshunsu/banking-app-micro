package bankingMicroservice.transactionservice.repository;

import bankingMicroservice.transactionservice.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
