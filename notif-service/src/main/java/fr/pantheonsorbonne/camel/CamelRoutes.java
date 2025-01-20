package fr.pantheonsorbonne.camel;

import fr.pantheonsorbonne.service.NotificationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.jboss.logging.Logger;

import java.util.Map;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    private static final Logger LOG = Logger.getLogger(CamelRoutes.class);

    @Inject
    NotificationService notificationService;

    @Override
    public void configure() {
        from("sjms2:M1.NotifService")
                .log("Réception d'une demande de notification : ${body}")
                .process(exchange -> {
                    // Assurez-vous que le corps est traité comme un Map
                    Map<String, Object> notificationDetails = exchange.getIn().getBody(Map.class);

                    if (notificationDetails != null) {
                        String recipientEmail = (String) notificationDetails.get("userEmail");
                        Long reservationNumber = Long.valueOf(notificationDetails.get("reservationNumber").toString());

                        if (recipientEmail != null && reservationNumber != null) {
                            // Appeler le service pour envoyer l'e-mail
                            notificationService.sendNotification(recipientEmail, reservationNumber);
                        } else {
                            LOG.error("Les informations de notification sont incomplètes.");
                        }
                    } else {
                        LOG.error("Le corps du message est vide ou incorrect.");
                    }
                })
                .log("Notification traitée avec succès.");
    }


}
