package com.haulmont.testtask.services.impls;

import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.repositories.CreditRepository;
import com.haulmont.testtask.services.CreditService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreditServiceImpl implements CreditService {
    final CreditRepository repository;

    public CreditServiceImpl(CreditRepository repository) {
        this.repository = repository;
    }

    public Credit getById(UUID id){
        return repository.getById(id);
    }

    public List<Credit> getAll(){
        return repository.findAll();
    }

    public boolean createCredit(Credit credit){
        repository.save(credit);
        return true;
    }

    public boolean deleteCredit(Credit credit){
        repository.deleteById(credit.getId());
        return true;
    }
}