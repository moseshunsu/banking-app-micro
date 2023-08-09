package bankingMicroservice.transactionservice.service.client;

import bankingMicroservice.transactionservice.dto.Response;
import bankingMicroservice.transactionservice.dto.TransactionRequest;
import bankingMicroservice.transactionservice.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "IDENTITY-SERVICE")
public interface IdentityFeignClient {
    @GetMapping("api/users/user/{accountNumber}")
    User fetchUser(@PathVariable("accountNumber") String accountNumber);

    @PostMapping("api/users/credit")
    ResponseEntity<Response> credit(@RequestBody TransactionRequest transactionRequest);

    @PostMapping("api/users/debit")
    ResponseEntity<Response> debit(@RequestBody TransactionRequest transactionRequest);

}
