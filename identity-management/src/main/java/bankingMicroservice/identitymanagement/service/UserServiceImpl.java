package bankingMicroservice.identitymanagement.service;

import bankingMicroservice.identitymanagement.dto.*;
import bankingMicroservice.identitymanagement.entity.User;
import bankingMicroservice.identitymanagement.rabbitmq.publisher.RabbitMQJsonProducer;
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

    @Autowired
    private RabbitMQJsonProducer jsonProducer;

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

        EmailDetails emailDetails = createRegistrationEmail(user);
        jsonProducer.sendJsonMessage(emailDetails);

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

    private EmailDetails createRegistrationEmail(User user) {
        return EmailDetails.builder()
                .subject("ACCOUNT CREATION")
                .recipient(user.getEmail())
                .messageBody(
                        "Dear " + user.getFirstName().toUpperCase() + " " +
                                user.getOtherName().toUpperCase() + " " +
                                user.getLastName().toUpperCase() +
                                ", your account has been successful created. Your Acc. No. is " +
                                user.getAccountNumber() + " and your balance is N" +
                                user.getAccountBalance() + "." +
                                "\n\nKindly note that this is demo mail, and that it exists sorely for test " +
                                "purposes." +
                                "\n\n\nBest Regards, \n\nMoses Hunsu."
                )
                //.attachment("C:\\Users\\moses.hunsu\\Documents\\Account test demo.txt")
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
    public ResponseEntity<Response> credit(TransactionRequest transactionRequest) {
        User receivingUser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        receivingUser.setAccountBalance(receivingUser.getAccountBalance().add(transactionRequest.getAmount()));
        userRepository.save(receivingUser);
        return createSuccessResponse(receivingUser);
    }

    @Override
    public ResponseEntity<Response> debit(TransactionRequest transactionRequest) {
        User debitingUser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());

        if (debitingUser.getAccountBalance().compareTo(transactionRequest.getAmount()) <= 0) {

            return ResponseEntity.ok().body(Response.builder()
                    .responseMessage(ResponseUtils.UNSUCCESSFUL_TRANSACTION)
                    .responseCode(ResponseUtils.INSUFFICIENT_BALANCE)
                    .data(Data.builder()
                            .accountName(debitingUser.getFirstName() + " " + debitingUser.getLastName())
                            .accountBalance(debitingUser.getAccountBalance())
                            .accountNumber(debitingUser.getAccountNumber())
                            .build())
                    .build());

        } else {
            debitingUser.setAccountBalance(debitingUser.getAccountBalance().subtract
                    (transactionRequest.getAmount()));

            userRepository.save(debitingUser);
            return createSuccessResponse(debitingUser);
        }

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

    @Override
    public User fetchUser(String accountNumber) {
        return userRepository.existsByAccountNumber(accountNumber)
                ? userRepository.findByAccountNumber(accountNumber)
                : null;

    }

    private ResponseEntity<Response> createSuccessResponse(User user) {
        Data userData = Data.builder()
                .accountBalance(user.getAccountBalance())
                .accountNumber(user.getAccountNumber())
                .accountName(user.getFirstName() + " " + user.getLastName())
                .build();

        return ResponseEntity.ok(Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage("SUCCESSFUL")
                .data(userData)
                .build());
    }

}
