package fr.pantheonsorbonne.service;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
@Named("paymentValidator")
@RegisterForReflection
public class PaymentValidator {

    public void validatePaymentDetails(String cardNumber, String expirationDate, String cvc, String cardHolderName) {
        if (!isCardNumberValid(cardNumber)) {
            throw new IllegalArgumentException("Numéro de carte invalide");
        }
        if (isCardExpired(expirationDate)) {
            throw new IllegalArgumentException("Carte expirée");
        }
        if (!isCvcValid(cvc)) {
            throw new IllegalArgumentException("CVC invalide");
        }
        if (!isCardHolderNameValid(cardHolderName)) {
            throw new IllegalArgumentException("Nom du titulaire invalide");
        }
    }

    private boolean isCardNumberValid(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    private boolean isCardExpired(String expirationDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth expiration = YearMonth.parse(expirationDate, formatter);
        return expiration.isBefore(YearMonth.now());
    }

    private boolean isCvcValid(String cvc) {
        return cvc != null && cvc.matches("\\d{3,4}");
    }

    private boolean isCardHolderNameValid(String cardHolderName) {
        return cardHolderName != null && !cardHolderName.trim().isEmpty();
    }
}
