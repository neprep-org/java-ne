package rw.bnr.javane.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {

    private Long id;
    private Long customerId; // To link to the Customer entity
    private String account;
    private double amount;
    private LocalDateTime bankingDate;
    private Long recipientId; // To link to the recipient Customer entity
}
