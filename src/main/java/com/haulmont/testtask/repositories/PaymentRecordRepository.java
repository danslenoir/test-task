package com.haulmont.testtask.repositories;

import com.haulmont.testtask.models.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, UUID> {
    @Override
    PaymentRecord getById(UUID uuid);

    @Override
    List<PaymentRecord> findAll();

}
