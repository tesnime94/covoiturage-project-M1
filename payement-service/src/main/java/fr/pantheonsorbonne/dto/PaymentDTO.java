package fr.pantheonsorbonne.dto;

import java.time.LocalDateTime;

public record PaymentDTO(
        Long id,
        Long trajetNumber,
        String paymentNumber,
        String amount,
        LocalDateTime paymentDate,
        String paymentStatus,
        String cardHolderName
) {}
