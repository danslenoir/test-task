package com.haulmont.testtask.services.impls;

import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.PaymentRecord;
import com.haulmont.testtask.models.PaymentSchedule;
import com.haulmont.testtask.repositories.PaymentScheduleRepository;
import com.haulmont.testtask.services.PaymentScheduleService;
import com.haulmont.testtask.services.paymentCalculators.PaymentCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentScheduleServiceImpl implements PaymentScheduleService {
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final PaymentRecordServiceImpl paymentRecordService;
    private final PaymentCalculator paymentCalculator;

    public PaymentScheduleServiceImpl(PaymentScheduleRepository paymentScheduleRepository, PaymentRecordServiceImpl paymentRecordService, PaymentCalculator paymentCalculator){
        this.paymentScheduleRepository = paymentScheduleRepository;
        this.paymentRecordService = paymentRecordService;
        this.paymentCalculator = paymentCalculator;
    }

    public boolean create(PaymentSchedule paymentSchedule){
        paymentScheduleRepository.save(paymentSchedule);
        return true;
    }

    public boolean delete(PaymentSchedule paymentSchedule){
        paymentScheduleRepository.deleteById(paymentSchedule.getId());
        return true;
    }

    public boolean update(PaymentSchedule paymentSchedule){
        paymentScheduleRepository.save(paymentSchedule);
        return true;
    }

    public List<PaymentRecord> calculatePaymentRecords(Credit credit, BigDecimal creditAmount, Integer creditTerm){
        return paymentCalculator.getFilledPaymentRecordsForCredit(credit, creditAmount, creditTerm);
    }

    public boolean createScheduleWithPaymentRecords(PaymentSchedule paymentSchedule, List<PaymentRecord> paymentRecords){
        for (PaymentRecord pR: paymentRecords){
            paymentRecordService.create(pR);
        }
        paymentSchedule.setPaymentRecords(paymentRecords);
        create(paymentSchedule);
        return true;
    }

}