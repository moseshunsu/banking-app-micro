package bankingMicroservice.transactionservice.service;

import bankingMicroservice.transactionservice.dto.Response;
import bankingMicroservice.transactionservice.dto.TransactionDto;
import bankingMicroservice.transactionservice.dto.TransactionRequest;
import bankingMicroservice.transactionservice.dto.TransferRequest;
import bankingMicroservice.transactionservice.entity.Transaction;
import bankingMicroservice.transactionservice.repository.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(TransactionDto transaction) {

        Transaction newTransaction = Transaction.builder()
                .transactionType(transaction.getTransactionType())
                .accountNumber(transaction.getAccountNumber())
                .amount(transaction.getAmount())
                .build();

        transactionRepository.save(newTransaction);
    }

    @Override
    public ResponseEntity<Response> credit(TransactionRequest transactionRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Response> debit(TransactionRequest transactionRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Response> transfer(TransferRequest transferRequest) {
        return null;
    }
}
