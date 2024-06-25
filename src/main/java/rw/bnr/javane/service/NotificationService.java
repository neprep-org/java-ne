package rw.bnr.javane.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.bnr.javane.model.Customer;
import rw.bnr.javane.model.Message;
import rw.bnr.javane.model.Transaction;
import rw.bnr.javane.model.Transfer;
import rw.bnr.javane.repository.MessageRepository;

import java.time.LocalDateTime;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendTransactionNotification(Customer customer, Transaction transaction) {
        String messageContent = String.format("Dear %s, your %s of amount %s on your account %s has been completed successfully.",
                customer.getFirstName(), transaction.getType(), transaction.getAmount(), transaction.getAccount());
        sendMessage(customer, messageContent);
        sendEmail(customer.getEmail(), "Transaction Notification", messageContent);
    }

    public void sendTransferNotification(Customer customer, Customer recipient, Transfer transfer) {
        String messageContent = String.format("Dear %s, your transfer of amount %s to account %s has been completed successfully.",
                customer.getFirstName(), transfer.getAmount(), recipient.getAccount());
        sendMessage(customer, messageContent);
        sendEmail(customer.getEmail(), "Transfer Notification", messageContent);

        String recipientMessageContent = String.format("Dear %s, you have received a transfer of amount %s from account %s.",
                recipient.getFirstName(), transfer.getAmount(), customer.getAccount());
        sendMessage(recipient, recipientMessageContent);
        sendEmail(recipient.getEmail(), "Transfer Notification", recipientMessageContent);
    }

    private void sendMessage(Customer customer, String content) {
        Message message = Message.builder()
                .customer(customer)
                .message(content)
                .dateTime(LocalDateTime.now())
                .build();
        messageRepository.save(message);
    }

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(content);
        mailSender.send(email);
    }
}
