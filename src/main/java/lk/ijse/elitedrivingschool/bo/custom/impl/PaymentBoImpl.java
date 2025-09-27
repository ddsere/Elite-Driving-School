package lk.ijse.elitedrivingschool.bo.custom.impl;

import lk.ijse.elitedrivingschool.bo.custom.PaymentBo;
import lk.ijse.elitedrivingschool.dao.custom.PaymentDao;
import lk.ijse.elitedrivingschool.dao.custom.impl.PaymentDaoImpl;
import lk.ijse.elitedrivingschool.dao.custom.StudentDao;
import lk.ijse.elitedrivingschool.dao.custom.impl.StudentDaoImpl;
import lk.ijse.elitedrivingschool.dto.PaymentDto;
import lk.ijse.elitedrivingschool.entity.Payment;
import lk.ijse.elitedrivingschool.entity.Student;
import lk.ijse.elitedrivingschool.exception.PaymentException;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentBoImpl implements PaymentBo {

    private final PaymentDao paymentDao = new PaymentDaoImpl();
    private final StudentDao studentDao = new StudentDaoImpl();

    @Override
    public void processPayment(PaymentDto paymentDto) throws PaymentException {
        Student student = studentDao.findById(paymentDto.getStudentId())
                .orElseThrow(() -> new PaymentException("Student not found for payment processing."));

        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setAmount(paymentDto.getAmount());
        payment.setPaymentDate(paymentDto.getPaymentDate());
        payment.setStatus(paymentDto.getStatus());

        paymentDao.save(payment);
    }

    @Override
    public List<PaymentDto> getPaymentsByStudent(Integer studentId) {
        return paymentDao.findByStudentId(studentId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentDao.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PaymentDto mapToDto(Payment payment) {
        return new PaymentDto(
                payment.getPaymentId(),
                payment.getStudent().getStudentId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getStatus()
        );
    }
}