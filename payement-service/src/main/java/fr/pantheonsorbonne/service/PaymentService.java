package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.PaymentDAO;
import fr.pantheonsorbonne.dto.PaymentDTO;
import fr.pantheonsorbonne.dto.ReservationDTO;
import fr.pantheonsorbonne.entity.Payment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Named("payment")
@ApplicationScoped
public class PaymentService {

    @Inject
    PaymentDAO paymentDAO;

    @Inject
    PaymentValidator paymentValidator;
    public Long savePayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setTrajetNumber(paymentDTO.trajetNumber());
        payment.setPaymentNumber(paymentDTO.paymentNumber());
        payment.setAmount(new BigDecimal(paymentDTO.amount()));
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(paymentDTO.paymentStatus());
        payment.setCardHolderName(paymentDTO.cardHolderName());

        paymentDAO.savePayment(payment);
        return payment.getId();
    }

    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentDAO.getById(id);
        if (payment == null) {
            return null;
        }

        return new PaymentDTO(
                payment.getId(),
                payment.getTrajetNumber(),
                payment.getPaymentNumber(),
                payment.getAmount().toString(),
                payment.getPaymentDate(),
                payment.getPaymentStatus(),
                payment.getCardHolderName()
        );
    }
    public boolean processPayment(ReservationDTO paymentDTO) {
        try {
            paymentValidator.validatePaymentDetails(
                    paymentDTO.cardNumber().toString(),
                    paymentDTO.expirationDate(),
                    String.valueOf(paymentDTO.cvc()),
                    paymentDTO.cardHolderName()
            );

            Payment payment = new Payment();
            payment.setTrajetNumber(paymentDTO.trajetNumber());
            payment.setPaymentNumber(generatePaymentNumber());
            payment.setAmount(BigDecimal.valueOf(paymentDTO.amount()));
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentStatus("SUCCESS");
            payment.setCardHolderName(paymentDTO.cardHolderName());

            paymentDAO.savePayment(payment);
            return true;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }


    private String generatePaymentNumber() {
        return "PAY-" + System.currentTimeMillis();
    }
}
