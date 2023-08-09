package bankingMicroservice.identitymanagement.service;

import bankingMicroservice.identitymanagement.dto.Response;
import bankingMicroservice.identitymanagement.dto.TransactionRequest;
import bankingMicroservice.identitymanagement.dto.UserRequest;
import bankingMicroservice.identitymanagement.entity.User;
import org.bouncycastle.cms.Recipient;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResponseEntity<Response> registerUser(UserRequest userRequest);
     User fetchUser(String accountNumber);
    List<Response> allUsers();
    ResponseEntity<Response> fetchUser(Long userId);
    ResponseEntity<Response> credit(TransactionRequest transactionRequest);
    ResponseEntity<Response> debit(TransactionRequest transactionRequest);
    ResponseEntity<Response> balanceEnquiry(String accountNumber);
    ResponseEntity<Response> nameEnquiry(String accountNumber);
}
