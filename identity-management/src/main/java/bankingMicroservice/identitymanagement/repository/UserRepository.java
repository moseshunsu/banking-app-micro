package bankingMicroservice.identitymanagement.repository;

import bankingMicroservice.identitymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String AccountNumber);
    User findByAccountNumber(String AccountNumber);
}
