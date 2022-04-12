package com.haulmont.testtask.services.impls;

import com.haulmont.testtask.models.CreditProposition;
import com.haulmont.testtask.models.PaymentRecord;
import com.haulmont.testtask.models.PaymentSchedule;
import com.haulmont.testtask.repositories.CreditPropositionRepository;
import com.haulmont.testtask.services.CreditPropositionService;
import com.haulmont.testtask.services.paymentCalculators.PaymentCalculator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreditPropositionServiceImpl implements CreditPropositionService {
    final CreditPropositionRepository creditPropositionRepository;
    final PaymentScheduleServiceImpl paymentScheduleService;
    final PaymentCalculator paymentCalculator;

    public CreditPropositionServiceImpl(CreditPropositionRepository creditPropositionRepository,
                                        PaymentScheduleServiceImpl paymentScheduleService, PaymentCalculator calculator) {
        this.creditPropositionRepository = creditPropositionRepository;
        this.paymentScheduleService = paymentScheduleService;
        this.paymentCalculator = calculator;
    }

    public List<CreditProposition> getAll(){
        return creditPropositionRepository.findAll();
    }

    public boolean delete(CreditProposition creditProposition){
        creditPropositionRepository.delete(creditProposition);
        return true;
    }

    public CreditProposition getById(UUID id){
        return creditPropositionRepository.getById(id);
    }

    public boolean createWithScheduleAndPaymentRecords(CreditProposition creditProposition){
        PaymentSchedule paymentSchedule = new PaymentSchedule();
        List<PaymentRecord> paymentRecords = paymentScheduleService.calculatePaymentRecords(creditProposition.getCredit(),
                creditProposition.getCreditAmount(), creditProposition.getCreditTerm());
        creditProposition.setTotalInterests(
                paymentCalculator.calculateTotalInterestsOnTheLoan(creditProposition.getCreditAmount(), paymentRecords)
        );
        paymentScheduleService.createScheduleWithPaymentRecords(paymentSchedule, paymentRecords);
        creditProposition.setPaymentSchedule(paymentSchedule);
        creditPropositionRepository.save(creditProposition);
        return true;
    }

    public boolean create(CreditProposition creditProposition) {
        creditPropositionRepository.save(creditProposition);
        return true;
    }
}