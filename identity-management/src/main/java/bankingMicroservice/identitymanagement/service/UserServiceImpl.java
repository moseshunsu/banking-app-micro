package bankingMicroservice.identitymanagement.service;

import bankingMicroservice.identitymanagement.dto.Data;
import bankingMicroservice.identitymanagement.dto.Response;
import bankingMicroservice.identitymanagement.dto.UserRequest;
import bankingMicroservice.identitymanagement.entity.User;
import bankingMicroservice.identitymanagement.repository.UserRepository;
import bankingMicroservice.identitymanagement.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements  UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Response> registerUser(UserRequest userRequest) {
        String email = userRequest.getEmail();

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                    .build());
        }

        User user = createUserFromRequest(userRequest);
        User savedUser = userRepository.save(user);

        return createSuccessResponse(savedUser);
    }

    private User createUserFromRequest(UserRequest userRequest) {
        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.LENGTH_OF_ACCOUNT_NUMBER))
                .accountBalance(userRequest.getAccountBalance())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .dateOfBirth(userRequest.getDateOfBirth())
                .build();
    }

    @Override
    public List<Response> allUsers() {
        List<User> usersList = userRepository.findAll();

        return usersList.stream().map(user -> Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .build()
                ).build()
        ).collect(Collectors.toList());

    }

    @Override
    public ResponseEntity<Response> fetchUser(Long userId) {

        return userRepository.findById(userId)
                .map(this::createSuccessResponse)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.builder()
                                .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                                .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE).build()));

    }

    @Override
    public ResponseEntity<Response> balanceEnquiry(String accountNumber) {
        return Optional.ofNullable(userRepository.findByAccountNumber(accountNumber))
                .map(this::createSuccessResponse)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.builder()
                                .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                                .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                                .build()));
    }

    @Override
    public ResponseEntity<Response> nameEnquiry(String accountNumber) {
        return Optional.ofNullable(userRepository.findByAccountNumber(accountNumber))
                .map(this::createSuccessResponse)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.builder()
                                .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                                .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                                .build()));
    }

    private ResponseEntity<Response> createSuccessResponse(User user) {
        Data userData = Data.builder()
                .accountBalance(user.getAccountBalance())
                .accountNumber(user.getAccountNumber())
                .accountName(user.getFirstName() + " " + user.getLastName())
                .build();

        return ResponseEntity.ok(Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.USER_SUCCESS_MESSAGE)
                .data(userData)
                .build());
    }

}
