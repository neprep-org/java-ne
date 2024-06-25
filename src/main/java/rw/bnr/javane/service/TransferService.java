package rw.bnr.javane.service;

import rw.bnr.javane.dto.TransferDTO;
import rw.bnr.javane.exceptions.InvalidRequestException;
import rw.bnr.javane.exceptions.ResourceNotFoundException;
import rw.bnr.javane.model.Customer;
import rw.bnr.javane.model.Transfer;
import rw.bnr.javane.repository.CustomerRepository;
import rw.bnr.javane.repository.TransferRepository;
import rw.bnr.javane.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private NotificationService notificationService;

    public List<TransferDTO> getAllTransfers() {
        return transferRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TransferDTO getTransferById(Long id) {
        Transfer transfer = transferRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transfer not found with id " + id));
        return convertToDTO(transfer);
    }

    public TransferDTO saveTransfer(TransferDTO transferDTO) {
        Customer sender = customerRepository.findById(transferDTO.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Sender not found with id " + transferDTO.getCustomerId()));
        Customer recipient = customerRepository.findById(transferDTO.getRecipientId()).orElseThrow(() -> new ResourceNotFoundException("Recipient not found with id " + transferDTO.getRecipientId()));

        if (sender.getBalance() < transferDTO.getAmount()) {
            throw new InvalidRequestException("Insufficient balance for transfer");
        }

        Transfer transfer = convertToEntity(transferDTO);
        transfer.setCustomer(sender);
        transfer.setRecipient(recipient);
        transfer.setBankingDate(LocalDateTime.now());

        sender.setBalance(sender.getBalance() - transferDTO.getAmount());
        recipient.setBalance(recipient.getBalance() + transferDTO.getAmount());

        customerRepository.save(sender);
        customerRepository.save(recipient);
        Transfer savedTransfer = transferRepository.save(transfer);

        // send notification to the user
        notificationService.sendTransferNotification(sender, recipient, savedTransfer);
        return convertToDTO(savedTransfer);
    }

    private TransferDTO convertToDTO(Transfer transfer) {
        return TransferDTO.builder()
                .id(transfer.getId())
                .customerId(transfer.getCustomer().getId())
                .account(transfer.getAccount())
                .amount(transfer.getAmount())
                .recipientId(transfer.getRecipient().getId())
                .bankingDate(transfer.getBankingDate())
                .build();
    }

    private Transfer convertToEntity(TransferDTO transferDTO) {
        return Transfer.builder()
                .account(transferDTO.getAccount())
                .amount(transferDTO.getAmount())
                .bankingDate(LocalDateTime.now())
                .build();
    }
}
