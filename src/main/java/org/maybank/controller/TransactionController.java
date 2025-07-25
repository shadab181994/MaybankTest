package org.maybank.controller;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.maybank.dto.TransactionDTO;
import org.maybank.entity.Transaction;
import org.maybank.repository.TransactionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRepository repository;


    @GetMapping
    public Page<Transaction> getAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String customerId)
            {
                Pageable pageable = PageRequest.of(page, size);

                if (keyword != null && !keyword.isEmpty()){
                    List<Transaction> result = repository.searchByKeyword(keyword);
                    return new PageImpl<>(result);
                }

                if (customerId != null && !customerId.isEmpty()){
                    List<Transaction> result = repository.findByCustomerId(customerId);
                    return new PageImpl<>(result);
                }
                return repository.findAll(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDescription(@PathVariable Long id,
                                               @RequestBody TransactionDTO dto) {
        try{
            Transaction t = repository.findById(id).orElseThrow();
            t.setDescription(dto.getDescription());
            repository.save(t);
            return ResponseEntity.ok(t);
        }catch (OptimisticLockException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Consurrent update detected");
        }

    }
}
