package com.biometricatm.controller;

import com.biometricatm.dto.DepositRequest;
import com.biometricatm.dto.TransferRequest;
import com.biometricatm.dto.WithdrawRequest;
import com.biometricatm.entity.Account;
import com.biometricatm.entity.Transaction;
import com.biometricatm.entity.User;
import com.biometricatm.enums.TransactionType;
import com.biometricatm.repository.AccountRepository;
import com.biometricatm.repository.TransactionRepository;
import com.biometricatm.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/account-details")
    public ResponseEntity<?> getAccountDetails(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        
        User fullUser = userRepository.findByFingerprintId(user.getFingerprintId()).orElse(null);
        if (fullUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        // Manually fetch accounts and transactions to avoid infinite recursion
        var accounts = accountRepository.findByUserId(fullUser.getId());
        var transactions = transactionRepository.findByUserId(fullUser.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User found");
        response.put("user", Map.of(
            "id", fullUser.getId(),
            "name", fullUser.getName(),
            "fingerprintId", fullUser.getFingerprintId(),
            "accounts", accounts,
            "transactions", transactions
        ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/account/withdraw")
    public ResponseEntity<?> withdraw(@Valid @RequestBody WithdrawRequest request, HttpServletRequest httpRequest) {
        User user = (User) httpRequest.getAttribute("user");
        
        User fullUser = userRepository.findByFingerprintId(user.getFingerprintId()).orElse(null);
        if (fullUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        Account account = accountRepository.findByUserIdAndBankName(fullUser.getId(), request.getBankName()).orElse(null);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Account not found"));
        }

        if (!account.getPin().equals(request.getPin())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid PIN"));
        }

        if (account.getBalance() < request.getAmt()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Insufficient balance"));
        }

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.withdraw);
        transaction.setUserId(fullUser.getId());
        transaction.setAmount(request.getAmt());
        transaction.setFromAccountNumber(account.getAccountNumber());
        transaction.setToAccountNumber(account.getAccountNumber());
        transactionRepository.save(transaction);

        // Update account balance
        account.setBalance(account.getBalance() - request.getAmt());
        accountRepository.save(account);

        // Get updated user with accounts and transactions
        User updatedUser = userRepository.findByFingerprintId(user.getFingerprintId()).get();
        var updatedAccounts = accountRepository.findByUserId(updatedUser.getId());
        var updatedTransactions = transactionRepository.findByUserId(updatedUser.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Withdrawl successful");
        response.put("user", Map.of(
            "id", updatedUser.getId(),
            "name", updatedUser.getName(),
            "fingerprintId", updatedUser.getFingerprintId(),
            "accounts", updatedAccounts,
            "transactions", updatedTransactions
        ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<?> deposit(@Valid @RequestBody DepositRequest request, HttpServletRequest httpRequest) {
        User user = (User) httpRequest.getAttribute("user");
        
        User fullUser = userRepository.findByFingerprintId(user.getFingerprintId()).orElse(null);
        if (fullUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        Account account = accountRepository.findByUserIdAndBankName(fullUser.getId(), request.getBankName()).orElse(null);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Account not found"));
        }

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.deposit);
        transaction.setUserId(fullUser.getId());
        transaction.setAmount(request.getAmt());
        transaction.setFromAccountNumber(account.getAccountNumber());
        transaction.setToAccountNumber(account.getAccountNumber());
        transactionRepository.save(transaction);

        // Update account balance
        account.setBalance(account.getBalance() + request.getAmt());
        accountRepository.save(account);

        // Get updated user with accounts and transactions
        User updatedUser = userRepository.findByFingerprintId(user.getFingerprintId()).get();
        var updatedAccounts = accountRepository.findByUserId(updatedUser.getId());
        var updatedTransactions = transactionRepository.findByUserId(updatedUser.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Deposit successful");
        response.put("user", Map.of(
            "id", updatedUser.getId(),
            "name", updatedUser.getName(),
            "fingerprintId", updatedUser.getFingerprintId(),
            "accounts", updatedAccounts,
            "transactions", updatedTransactions
        ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/account/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest request) {
        Account senderAccount = accountRepository.findByAccountNumber(request.getSenderAccountNO()).orElse(null);
        if (senderAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Sender account not found"));
        }

        if (!senderAccount.getPin().equals(request.getPin())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid PIN"));
        }

        if (senderAccount.getBalance() < request.getAmt()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Insufficient balance"));
        }

        Account receiverAccount = accountRepository.findByAccountNumber(request.getReceiverAccountNO()).orElse(null);
        if (receiverAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Receiver account not found"));
        }

        // Update balances
        senderAccount.setBalance(senderAccount.getBalance() - request.getAmt());
        receiverAccount.setBalance(receiverAccount.getBalance() + request.getAmt());
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.transfer);
        transaction.setFromAccountNumber(senderAccount.getAccountNumber());
        transaction.setToAccountNumber(receiverAccount.getAccountNumber());
        transaction.setAmount(request.getAmt());
        transaction.setUserId(senderAccount.getUserId());
        transactionRepository.save(transaction);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Transfer successful");
        response.put("senderAccountNo", senderAccount.getAccountNumber());
        response.put("receiverAccountNo", receiverAccount.getAccountNumber());
        response.put("transaction", transaction);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/statement/{accountNumber}")
    public ResponseEntity<?> getAccountStatement(@PathVariable String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Account Number is required"));
        }

        Account account = accountRepository.findByAccountNumber(accountNumber).orElse(null);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Account not found"));
        }

        List<Transaction> transactions = transactionRepository.findByUserId(account.getUserId());
        return ResponseEntity.ok(Map.of("transactions", transactions));
    }
}
