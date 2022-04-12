package com.haulmont.testtask.services;

import com.haulmont.testtask.models.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    boolean createClient(Customer customer);
    boolean deleteClient(Customer customer);
    List<Customer> getAll();
    Customer getById(UUID id);
}
