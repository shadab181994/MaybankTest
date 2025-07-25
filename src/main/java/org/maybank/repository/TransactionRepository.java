package org.maybank.repository;

import org.maybank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import  java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository <Transaction, Long> {

    List<Transaction> findByCustomerId(String customerId);

    @Query("SELECT t FROM Transaction t WHERE t.accountNumber Like %:keyword% OR t.description LIKE %:keyword%")
    List<Transaction> searchByKeyword(String keyword);

    Optional<Transaction> findById(Long id);



}
