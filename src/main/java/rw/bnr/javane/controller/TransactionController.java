package rw.bnr.javane.controller;

import jakarta.validation.Valid;
import rw.bnr.javane.dto.TransactionDTO;
import rw.bnr.javane.exceptions.InvalidRequestException;
import rw.bnr.javane.exceptions.ResourceNotFoundException;
import rw.bnr.javane.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
            return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction( @RequestBody TransactionDTO transactionDTO) {
            return ResponseEntity.status(201).body(transactionService.saveTransaction(transactionDTO));
    }
}
