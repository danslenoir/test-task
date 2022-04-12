package com.haulmont.testtask.repositories;

import com.haulmont.testtask.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,UUID> {
    Customer getById(UUID id);

    List<Customer> findAll();

    @Override
    void deleteById(UUID uuid);
}
