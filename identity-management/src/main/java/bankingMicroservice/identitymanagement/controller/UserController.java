package bankingMicroservice.identitymanagement.controller;

import bankingMicroservice.identitymanagement.dto.Response;
import bankingMicroservice.identitymanagement.dto.UserRequest;
import bankingMicroservice.identitymanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Response> registerUser(@RequestBody UserRequest userRequest) {
        return userService.registerUser(userRequest);
    }

    @GetMapping
    public List<Response> allUsers() {
        return userService.allUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Response> fetchUser(@PathVariable(name = "userId") Long userId) {
        return userService.fetchUser(userId);
    }

    @GetMapping("/balanceEnquiry")
    public ResponseEntity<Response> balanceEnquiry(@RequestParam(name = "accountNumber") String accountNumber) {
        return userService.balanceEnquiry(accountNumber);
    }

    @GetMapping("/nameEnquiry")
    public ResponseEntity<Response> nameEnquiry(@RequestParam (name = "accountNumber") String accountNumber) {
        return userService.nameEnquiry(accountNumber);
    }

}
