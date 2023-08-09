package bankingMicroservice.notificationservice.rabbitmq.consumer;

import bankingMicroservice.notificationservice.email.dto.EmailDetails;
import bankingMicroservice.notificationservice.email.service.EmailService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RabbitMQEmailConsumer {

    @Autowired
    private EmailService emailService;

    private static final int MAX_RETRY_ATTEMPTS = 5;

    @RabbitListener(queues = {"${rabbitmq.user.registration.queue}"})
    public void sendUserRegistrationDetails(EmailDetails emailDetails, Message message, Channel channel)
            throws IOException {

        int currentRetryAttempt = 0;

        while (currentRetryAttempt < MAX_RETRY_ATTEMPTS) {
            try {
                emailService.sendSimpleMail(emailDetails);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); // Acknowledge successful processing
            } catch (Exception e) {
                System.err.println("Error processing message: " + e.getMessage());
                moveToDLQ(message, channel);
                currentRetryAttempt++;
            }
        }
    }


    private void moveToDLQ(Message message, Channel channel) throws IOException {
        System.err.println("Message failed. Sending to DLQ.");

        // Configure DLX and DLR properties for the queue
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "dlx_exchange");     // Replace with your DLX exchange name
        arguments.put("x-dead-letter-routing-key", "dlq_routing_key"); // Replace with your DLR routing key

        // Declare the main queue with DLX and DLR properties
        channel.queueDeclare(
                "banking_microservice_queue",   // Replace with your main queue name
                true,
                false,
                false,
                arguments
        );

        // Publish the message to the main queue
        channel.basicPublish("", "banking_microservice_queue", null, message.getBody());

        // Reject the message from the main queue
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }

}
