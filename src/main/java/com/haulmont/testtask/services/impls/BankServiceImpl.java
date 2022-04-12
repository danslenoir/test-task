package com.haulmont.testtask.services.impls;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.repositories.BankRepository;
import com.haulmont.testtask.services.BankService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BankServiceImpl implements BankService {
    final BankRepository bankRepository;

    public BankServiceImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public List<Bank> getAll(){
        return bankRepository.findAll();
    }

    public boolean create(Bank bank){
        bankRepository.save(bank);
        return true;
    }

    public Bank getById(UUID id){
        return bankRepository.getById(id);
    }

    public boolean delete(Bank bank){
        bankRepository.deleteById(bank.getId());
        return true;
    }

    public boolean update(Bank bank){
        bankRepository.save(bank);
        return true;
    }
}