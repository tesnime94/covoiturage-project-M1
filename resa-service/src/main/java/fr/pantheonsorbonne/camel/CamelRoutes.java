package fr.pantheonsorbonne.camel;

import fr.pantheonsorbonne.dto.TrajetResaDTO;
import fr.pantheonsorbonne.gateway.ResaGateway;
import fr.pantheonsorbonne.service.ResaService;
import org.apache.camel.builder.RouteBuilder;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class CamelRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("direct:processPayment")
                .log("Envoi des détails du paiement au service Payment pour traitement")
                .marshal().json() // Conversion en JSON avant l'envoi
                .to("sjms2:M1.PayementService") // Envoi du message au service Payment via un broker
                .unmarshal().json() // Conversion de la réponse en objet utilisable
                .log("Réponse reçue : ${body}") // Log de la réponse reçue
                .choice()
                .when(body().isEqualTo(false))
                .log("Paiement échoué")
                .throwException(new RuntimeException("Le paiement a été refusé par le service Payment"))
                .otherwise()
                .log("Paiement validé avec succès")
                .end();

        from("direct:sendConfirmationNotification")
                .log("Envoi de la notification pour réservation : ${body}")
                 // Convertir le message en JSON
                .to("sjms2:M1.NotifService") // Envoi au microservice Notification via le broker
                .marshal().json()
                .log("Notification envoyée avec succès : ${body}");

        from("sjms2:M1.ResaService") // Écoute les messages sur la file
                .log("Réception des résultats de recherche : ${body}")
                .unmarshal().json(JsonLibrary.Jackson, Map.class) // Désérialiser en Map<String, Object>
                .process(exchange -> {
                    // Récupérer les données sous forme de Map
                    Map<String, Object> resultatMap = exchange.getIn().getBody(Map.class);

                    // Vérifier si c'est un trajet principal
                    boolean isTrajetPrincipal = (boolean) resultatMap.get("isTrajetPrincipal");

                    if (isTrajetPrincipal) {
                        // Si trajet principal, extraire le premier trajet
                        List<Map<String, Object>> trajets = (List<Map<String, Object>>) resultatMap.get("trajets");
                        Map<String, Object> trajet = trajets.get(0); // Choisir le premier
                        exchange.getIn().setBody(trajet);
                        exchange.getIn().setHeader("action", "createResa");
                    } else {
                        // Si sous-trajet, extraire le premier sous-trajet
                        List<Map<String, Object>> sousTrajets = (List<Map<String, Object>>) resultatMap.get("trajets");
                        Map<String, Object> sousTrajet = sousTrajets.get(0); // Choisir le premier
                        exchange.getIn().setBody(sousTrajet);
                        exchange.getIn().setHeader("action", "notifyDriver");
                    }
                })
                .choice()
                .when(header("action").isEqualTo("createResa"))
                .log("Création d'une réservation pour le trajet principal : ${body}")
                .bean(ResaService.class, "createResaFromMap") // Appeler une méthode utilisant Map
                .when(header("action").isEqualTo("notifyDriver"))
                .log("Notification pour le conducteur du sous-trajet : ${body}")
                .bean(ResaGateway.class, "sendDriverNotificationFromMap") // Méthode utilisant Map
                .otherwise()
                .log("Aucune action définie pour ce message.")
                .end();







    }
}
