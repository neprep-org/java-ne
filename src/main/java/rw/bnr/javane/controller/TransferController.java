package rw.bnr.javane.controller;

import jakarta.validation.Valid;
import rw.bnr.javane.dto.TransferDTO;
import rw.bnr.javane.exceptions.InvalidRequestException;
import rw.bnr.javane.exceptions.ResourceNotFoundException;
import rw.bnr.javane.service.TransferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @GetMapping
    public ResponseEntity<List<TransferDTO>> getAllTransfers() {
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferDTO> getTransferById(@PathVariable Long id) {
            return ResponseEntity.ok(transferService.getTransferById(id));
    }

    @PostMapping
    public ResponseEntity<TransferDTO> createTransfer( @RequestBody TransferDTO transferDTO) {
            return ResponseEntity.status(201).body(transferService.saveTransfer(transferDTO));
    }
}