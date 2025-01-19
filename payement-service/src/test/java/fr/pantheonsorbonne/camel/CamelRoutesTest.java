package fr.pantheonsorbonne.camel;

import fr.pantheonsorbonne.dto.ReservationDTO;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ConsumerTemplate;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CamelRoutesTest {

    @Inject
    CamelContext camelContext;

    @Inject
    ProducerTemplate producerTemplate;

    @Inject
    ConsumerTemplate consumerTemplate;

    @Test
    public void testPaymentAcceptedRoute() throws Exception {
        producerTemplate.sendBody("sjms2:M1.PayementService",
                "{ \"trajetNumber\": 123456, \"amount\": 15000, \"cardHolderName\": \"John Doe\", \"cardNumber\": 4532015112830366, \"expirationDate\": \"12/30\", \"cvc\": 123 }");

        // Consommation avec délai pour permettre le traitement
        Boolean output = consumerTemplate.receiveBody("sjms2:M1.ResaService", 5000, Boolean.class);

        // Vérifications
        assertNotNull(output, "Aucun message n'a été consommé sur la queue M1.ReservationService");
        assertEquals(true, output, "Le paiement devrait être accepté");
    }

    @Test
    public void testPaymentRefusedRoute() throws Exception {
        producerTemplate.sendBody("sjms2:M1.PayementService",
                "{ \"trajetNumber\": 123456, \"amount\": 15000, \"cardHolderName\": \"John Doe\", \"cardNumber\": 1234567890123456, \"expirationDate\": \"12/30\", \"cvc\": 123 }");

        // Consommation avec délai pour permettre le traitement
        Boolean output = consumerTemplate.receiveBody("sjms2:M1.ResaService", 5000, Boolean.class);

        // Vérifications
        assertNotNull(output, "Aucun message n'a été consommé sur la queue M1.ReservationService");
        assertEquals(false, output, "Le paiement devrait être refusé");
    }

}
