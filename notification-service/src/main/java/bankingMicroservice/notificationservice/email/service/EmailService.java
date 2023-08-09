package bankingMicroservice.notificationservice.email.service;

import bankingMicroservice.notificationservice.email.dto.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);
    String sendMailWithAttachment(EmailDetails details);

}
