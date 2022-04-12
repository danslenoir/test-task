package com.haulmont.testtask.repositories;

import com.haulmont.testtask.models.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CreditRepository extends JpaRepository<Credit, UUID> {
    Credit getById(UUID id);
    List<Credit> findAll();
    @Override
    void deleteById(UUID id);
}
