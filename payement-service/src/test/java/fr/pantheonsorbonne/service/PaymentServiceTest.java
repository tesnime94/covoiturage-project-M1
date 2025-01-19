package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dto.ReservationDTO;
import fr.pantheonsorbonne.entity.Payment;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PaymentServiceTest {

    @Inject
    PaymentService paymentService;

    @Test
    public void testProcessPayment_Success() {
        // Données valides pour le test
        ReservationDTO validPayment = new ReservationDTO(
                123456L,
                15000L,
                "John Doe",
                4111111111111111L,
                "12/30",
                123
        );

        // Appel de la méthode à tester
        boolean result = paymentService.processPayment(validPayment);

        // Vérification
        assertTrue(result, "Le paiement devrait être validé");
    }

    @Test
    public void testProcessPayment_InvalidCardNumber() {
        // Données avec un numéro de carte invalide
        ReservationDTO invalidCardPayment = new ReservationDTO(
                123456L,
                15000L,
                "John Doe",
                1234567890123456L,
                "12/30",
                123
        );

        // Appel de la méthode à tester
        boolean result = paymentService.processPayment(invalidCardPayment);

        // Vérification
        assertFalse(result, "Le paiement devrait échouer avec un numéro de carte invalide");
    }

    @Test
    public void testProcessPayment_CardExpired() {
        // Données avec une carte expirée
        ReservationDTO expiredCardPayment = new ReservationDTO(
                123456L,
                15000L,
                "John Doe",
                4111111111111111L,
                "12/20", // Date expirée
                123
        );

        // Appel de la méthode à tester
        boolean result = paymentService.processPayment(expiredCardPayment);

        // Vérification
        assertFalse(result, "Le paiement devrait échouer avec une carte expirée");
    }

    @Test
    public void testProcessPayment_InvalidCvc() {
        // Données avec un CVC invalide
        ReservationDTO invalidCvcPayment = new ReservationDTO(
                123456L,
                15000L,
                "John Doe",
                4111111111111111L,
                "12/30",
                12 // CVC invalide
        );

        // Appel de la méthode à tester
        boolean result = paymentService.processPayment(invalidCvcPayment);

        // Vérification
        assertFalse(result, "Le paiement devrait échouer avec un CVC invalide");
    }

    @Test
    public void testProcessPayment_InvalidCardHolderName() {
        // Données avec un nom vide
        ReservationDTO emptyCardHolder = new ReservationDTO(
                123456L,
                15000L,
                "",
                4111111111111111L,
                "12/30",
                123
        );

        // Appel de la méthode à tester
        boolean result = paymentService.processPayment(emptyCardHolder);

        // Vérification
        assertFalse(result, "Le paiement devrait échouer avec un nom vide");
    }
}
