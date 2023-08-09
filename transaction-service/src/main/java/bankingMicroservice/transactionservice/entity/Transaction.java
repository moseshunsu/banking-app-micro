package bankingMicroservice.transactionservice.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String transactionType;
    private String accountNumber;
    private BigDecimal amount;
//    @CreatedDate
    private LocalDateTime dateTime;
}
