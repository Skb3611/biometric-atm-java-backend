package com.biometricatm.controller;

import com.biometricatm.dto.FingerprintVerifyRequest;
import com.biometricatm.entity.User;
import com.biometricatm.repository.AccountRepository;
import com.biometricatm.repository.TransactionRepository;
import com.biometricatm.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/verify-fingerprint")
    public ResponseEntity<?> verifyFingerprint(@Valid @RequestBody FingerprintVerifyRequest request) {
        User user = userRepository.findByFingerprintId(request.getFingerprintId()).orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        // Manually fetch accounts and transactions to avoid infinite recursion
        var accounts = accountRepository.findByUserId(user.getId());
        var transactions = transactionRepository.findByUserId(user.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Fingerprint verified");
        response.put("user", Map.of(
            "id", user.getId(),
            "name", user.getName(),
            "fingerprintId", user.getFingerprintId(),
            "accounts", accounts,
            "transactions", transactions
        ));

        return ResponseEntity.ok(response);
    }
}
