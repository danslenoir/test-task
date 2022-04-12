package com.haulmont.testtask.repositories;

import com.haulmont.testtask.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankRepository extends JpaRepository<Bank, UUID> {
    @Override
    List<Bank> findAll();

    @Override
    Bank getById(UUID uuid);

    @Override
    void deleteById(UUID uuid);
}
