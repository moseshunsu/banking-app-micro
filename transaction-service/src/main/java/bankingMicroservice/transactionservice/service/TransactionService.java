package bankingMicroservice.transactionservice.service;

import bankingMicroservice.transactionservice.dto.Response;
import bankingMicroservice.transactionservice.dto.TransactionDto;
import bankingMicroservice.transactionservice.dto.TransactionRequest;
import bankingMicroservice.transactionservice.dto.TransferRequest;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
    ResponseEntity<Response> credit(TransactionRequest transactionRequest);
    ResponseEntity<Response> debit(TransactionRequest transactionRequest);
    ResponseEntity<Response> transfer(TransferRequest transferRequest);
}
