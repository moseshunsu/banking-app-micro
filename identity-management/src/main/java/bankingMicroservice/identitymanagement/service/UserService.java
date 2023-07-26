package bankingMicroservice.identitymanagement.service;

import bankingMicroservice.identitymanagement.dto.Response;
import bankingMicroservice.identitymanagement.dto.UserRequest;
import org.bouncycastle.cms.Recipient;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<Response> registerUser(UserRequest userRequest);
    List<Response> allUsers();
    ResponseEntity<Response> fetchUser(Long userId);
    ResponseEntity<Response> balanceEnquiry(String accountNumber);
    ResponseEntity<Response> nameEnquiry(String accountNumber);
}
