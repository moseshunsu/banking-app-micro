package bankingMicroservice.identitymanagement.rabbitmq.publisher;

import bankingMicroservice.identitymanagement.dto.EmailDetails;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQJsonProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.user.registration.key}")
    private String userRegistrationKey;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJsonMessage(EmailDetails emailDetails) {
        rabbitTemplate.convertAndSend(exchange, userRegistrationKey, emailDetails);
    }

}
