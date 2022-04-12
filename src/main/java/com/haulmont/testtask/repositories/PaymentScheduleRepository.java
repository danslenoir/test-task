package com.haulmont.testtask.repositories;

import com.haulmont.testtask.models.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, UUID>{
    @Override
    void deleteById(UUID uuid);
}