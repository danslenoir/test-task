package com.haulmont.testtask.repositories;

import com.haulmont.testtask.models.CreditProposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CreditPropositionRepository extends JpaRepository<CreditProposition, UUID> {
    @Override
    List<CreditProposition> findAll();

    @Override
    CreditProposition getById(UUID uuid);
}
