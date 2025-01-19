package fr.pantheonsorbonne.dto;

public record ResaDTO(Long reservationNumber, Long trajetNumber, Long amount, String cardHolderName, Long cardNumber, String expirationDate, int cvc) {
}
