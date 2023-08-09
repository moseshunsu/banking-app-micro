package bankingMicroservice.transactionservice.service;

import bankingMicroservice.transactionservice.dto.*;
import bankingMicroservice.transactionservice.entity.Transaction;
import bankingMicroservice.transactionservice.entity.User;
import bankingMicroservice.transactionservice.repository.TransactionRepository;
import bankingMicroservice.transactionservice.service.client.IdentityFeignClient;
import bankingMicroservice.transactionservice.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private IdentityFeignClient identityFeignClient;

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
        User fetchedUser = identityFeignClient.fetchUser(transactionRequest.getAccountNumber());

        if (fetchedUser == null) {
            return new ResponseEntity<>(Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(Data.builder()
                            .accountNumber(transactionRequest.getAccountNumber())
                            .build())
                    .build(), HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = Transaction.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .accountNumber(transactionRequest.getAccountNumber())
                .transactionType("CREDIT")
                .amount(transactionRequest.getAmount())
                .dateTime(LocalDateTime.now().plusHours(1L))
                .build();

        transactionRepository.save(transaction);

        return identityFeignClient.credit(transactionRequest);
    }

    @Override
    public ResponseEntity<Response> debit(TransactionRequest transactionRequest) {
        User fetchedUser = identityFeignClient.fetchUser(transactionRequest.getAccountNumber());

        if (fetchedUser == null) {
            return new ResponseEntity<>(Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(Data.builder()
                            .accountNumber(transactionRequest.getAccountNumber())
                            .build())
                    .build(), HttpStatus.BAD_REQUEST);
        }

        transactionRepository.save(
                Transaction.builder()
                        .accountNumber(transactionRequest.getAccountNumber())
                        .transactionType("DEBIT")
                        .amount(transactionRequest.getAmount())
                        .build()
        );

        return identityFeignClient.debit(transactionRequest);
    }

    @Override
    public ResponseEntity<Response> transfer(TransferRequest transferRequest) {
        return null;
    }
}
