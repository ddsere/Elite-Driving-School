package lk.ijse.elitedrivingschool.bo.custom;

import lk.ijse.elitedrivingschool.dto.PaymentDto;
import lk.ijse.elitedrivingschool.exception.PaymentException;

import java.util.List;

public interface PaymentBo {
    void processPayment(PaymentDto paymentDto) throws PaymentException;
    List<PaymentDto> getPaymentsByStudent(Integer studentId);
    List<PaymentDto> getAllPayments();
}
