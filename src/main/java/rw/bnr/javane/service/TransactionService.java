package rw.bnr.javane.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.bnr.javane.dto.TransactionDTO;
import rw.bnr.javane.exceptions.InvalidRequestException;
import rw.bnr.javane.exceptions.ResourceNotFoundException;
import rw.bnr.javane.model.Customer;
import rw.bnr.javane.model.Transaction;
import rw.bnr.javane.repository.CustomerRepository;
import rw.bnr.javane.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired NotificationService notificationService;

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + id));
        return convertToDTO(transaction);
    }

    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        Customer customer = customerRepository.findById(transactionDTO.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + transactionDTO.getCustomerId()));

        if ("withdraw".equals(transactionDTO.getType()) && customer.getBalance() < transactionDTO.getAmount()) {
            throw new InvalidRequestException("Insufficient balance for withdrawal");
        }

        Transaction transaction = convertToEntity(transactionDTO);
        transaction.setCustomer(customer);
        transaction.setBankingDate(LocalDateTime.now());

        if ("saving".equals(transactionDTO.getType())) {
            customer.setBalance(customer.getBalance() + transactionDTO.getAmount());
        } else if ("withdraw".equals(transactionDTO.getType())) {
            customer.setBalance(customer.getBalance() - transactionDTO.getAmount());
        }


        customerRepository.save(customer);
        Transaction savedTransaction = transactionRepository.save(transaction);

        // send notification to the user and save changes
        notificationService.sendTransactionNotification(customer, transaction);

        return convertToDTO(savedTransaction);
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .customerId(transaction.getCustomer().getId())
                .account(transaction.getAccount())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .bankingDate(transaction.getBankingDate())
                .build();
    }

    private Transaction convertToEntity(TransactionDTO transactionDTO) {
        return Transaction.builder()
                .account(transactionDTO.getAccount())
                .amount(transactionDTO.getAmount())
                .type(transactionDTO.getType())
                .bankingDate(LocalDateTime.now())
                .build();
    }
}