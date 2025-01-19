package fr.pantheonsorbonne.dto;

public record ReservationDTO(
        Long trajetNumber,
        Long amount,
        String cardHolderName,
        Long cardNumber,
        String expirationDate,
        int cvc
) {}
