package org.maybank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import javax.annotation.processing.SupportedSourceVersion;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private BigDecimal trxAmount;
    private String description;
    private LocalDate trxDate;
    private LocalTime trxTime;
    private String customerId;

    @Version
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate(){
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

}
