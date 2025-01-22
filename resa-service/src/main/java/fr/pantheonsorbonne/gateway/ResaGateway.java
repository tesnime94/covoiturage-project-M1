package fr.pantheonsorbonne.gateway;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.ProducerTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
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

    public void sendConfirmationNotification(String userEmail, Long reservationNumber) {
        Map<String, Object> notificationDetails = new HashMap<>();
        notificationDetails.put("userEmail", userEmail);
        notificationDetails.put("reservationNumber", reservationNumber);

        producerTemplate.sendBody("direct:sendConfirmationNotification", notificationDetails);
    }

    public void sendDriverNotificationFromMap(String userEmail, Long trajetNumber ) {
        Map<String, Object> notificationDetails2 = new HashMap<>();
        notificationDetails2.put("userEmail", userEmail);
        notificationDetails2.put("trajetNumber", trajetNumber);

        producerTemplate.sendBody("direct:sendConfirmationNotification", notificationDetails2);
    }

}
