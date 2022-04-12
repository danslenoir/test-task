package com.haulmont.testtask.services.impls;

import com.haulmont.testtask.repositories.CustomerRepository;
import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.services.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {
    final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public boolean createClient(Customer client){
        customerRepository.save(client);
        return true;
    }

    public boolean deleteClient(Customer client){
        customerRepository.deleteById(client.getId());
        return true;
    }

    public Customer getById(UUID id){
        return customerRepository.getById(id);
    }

    public List<Customer> getAll(){
        return customerRepository.findAll();
    }
}
