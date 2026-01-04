package com.biometricatm.config;

import com.biometricatm.entity.Account;
import com.biometricatm.entity.Transaction;
import com.biometricatm.entity.User;
import com.biometricatm.enums.BankName;
import com.biometricatm.enums.TransactionType;
import com.biometricatm.repository.AccountRepository;
import com.biometricatm.repository.TransactionRepository;
import com.biometricatm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only seed if database is empty
        if (userRepository.count() == 0) {
            seedData();
        }
    }

    private void seedData() {
        System.out.println("🌱 Starting seed...");

        // Clear existing data
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();

        // Create users
        User abhi = new User("Abhi", "fingerprint_abhi");
        User rohan = new User("Rohan", "fingerprint_rohan");
        User jonny = new User("Jonny", "fingerprint_jonny");

        abhi = userRepository.save(abhi);
        rohan = userRepository.save(rohan);
        jonny = userRepository.save(jonny);

        System.out.println("✅ Created 3 users");

        // Create accounts
        Account abhiSbi = new Account("1001234567", 1234, 50000, BankName.SBI, abhi.getId());
        Account abhiHdfc = new Account("1001234570", 1234, 30000, BankName.HDFC, abhi.getId());
        Account rohanIcici = new Account("1001234568", 5678, 75000, BankName.ICICI, rohan.getId());
        Account rohanAxis = new Account("1001234571", 5678, 45000, BankName.AXIS, rohan.getId());
        Account jonnyAxis = new Account("1001234572", 9012, 60000, BankName.AXIS, jonny.getId());

        abhiSbi = accountRepository.save(abhiSbi);
        abhiHdfc = accountRepository.save(abhiHdfc);
        rohanIcici = accountRepository.save(rohanIcici);
        rohanAxis = accountRepository.save(rohanAxis);
        jonnyAxis = accountRepository.save(jonnyAxis);

        System.out.println("✅ Created 5 accounts");

        // Create transactions
        Transaction transfer1 = new Transaction(rohanIcici.getAccountNumber(), abhiSbi.getAccountNumber(), 5000, TransactionType.transfer, abhi.getId());
        Transaction transfer2 = new Transaction(jonnyAxis.getAccountNumber(), rohanIcici.getAccountNumber(), 10000, TransactionType.transfer, rohan.getId());
        Transaction transfer3 = new Transaction(abhiSbi.getAccountNumber(), jonnyAxis.getAccountNumber(), 15000, TransactionType.transfer, jonny.getId());
        Transaction deposit1 = new Transaction(abhiHdfc.getAccountNumber(), abhiHdfc.getAccountNumber(), 20000, TransactionType.deposit, abhi.getId());
        Transaction withdraw1 = new Transaction(rohanAxis.getAccountNumber(), rohanAxis.getAccountNumber(), 8000, TransactionType.withdraw, rohan.getId());
        Transaction deposit2 = new Transaction(abhiSbi.getAccountNumber(), abhiSbi.getAccountNumber(), 25000, TransactionType.deposit, jonny.getId());

        transactionRepository.save(transfer1);
        transactionRepository.save(transfer2);
        transactionRepository.save(transfer3);
        transactionRepository.save(deposit1);
        transactionRepository.save(withdraw1);
        transactionRepository.save(deposit2);

        System.out.println("✅ Created 6 transactions");
        System.out.println("🎉 Seed completed successfully!");
    }
}
