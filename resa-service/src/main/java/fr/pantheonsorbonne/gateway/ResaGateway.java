package fr.pantheonsorbonne.gateway;

import jakarta.inject.Inject;
import org.apache.camel.ProducerTemplate;

import java.util.HashMap;
import java.util.Map;

public class ResaGateway {

    @Inject
    private ProducerTemplate producerTemplate;

    public boolean processPayment(Long trajetNumber, Long amount, String cardHolderName, Long cardNumber, String expirationDate, int cvc) {
        // Créer un objet simple ou utiliser un Map pour transmettre les données
        Map<String, Object> resaDetails = new HashMap<>();
        resaDetails.put("trajetNumber", trajetNumber);
        resaDetails.put("amount", amount);
        resaDetails.put("cardHolderName", cardHolderName);
        resaDetails.put("cardNumber", cardNumber);
        resaDetails.put("expirationDate", expirationDate);
        resaDetails.put("cvc", cvc);

        // Envoi des détails de paiement à la route Camel
        Object response = producerTemplate.requestBody("direct:processPayment", resaDetails);

        // Retourner la réponse du service Payment (true ou false)
        return Boolean.parseBoolean(response.toString());
    }
}
