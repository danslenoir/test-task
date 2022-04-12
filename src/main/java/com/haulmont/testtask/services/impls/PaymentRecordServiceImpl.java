package com.haulmont.testtask.services.impls;

import com.haulmont.testtask.models.PaymentRecord;
import com.haulmont.testtask.repositories.PaymentRecordRepository;
import com.haulmont.testtask.services.PaymentRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentRecordServiceImpl implements PaymentRecordService {
    private final PaymentRecordRepository paymentRecordRepository;

    public PaymentRecordServiceImpl(PaymentRecordRepository paymentRecordRepository) {
        this.paymentRecordRepository = paymentRecordRepository;
    }

    public boolean create(PaymentRecord paymentRecord){
        paymentRecordRepository.save(paymentRecord);
        return true;
    }

    public PaymentRecord getById(UUID id){
        return paymentRecordRepository.getById(id);
    }

    public boolean delete(PaymentRecord paymentRecord){
        paymentRecordRepository.delete(paymentRecord);
        return true;
    }

    public boolean createAll(List<PaymentRecord> paymentRecordList){
        paymentRecordRepository.saveAll(paymentRecordList);
        return true;
    }
}