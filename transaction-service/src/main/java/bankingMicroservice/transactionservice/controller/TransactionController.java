package bankingMicroservice.transactionservice.controller;

import bankingMicroservice.transactionservice.dto.Response;
import bankingMicroservice.transactionservice.dto.TransactionRequest;
import bankingMicroservice.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/credit")
    ResponseEntity<Response> credit(@RequestBody TransactionRequest transactionRequest) {
        return transactionService.credit(transactionRequest);
    }

    @PostMapping("/debit")
    ResponseEntity<Response> debit(@RequestBody TransactionRequest transactionRequest) {
        return transactionService.debit(transactionRequest);
    }

}
