package fr.pantheonsorbonne.camel;

import fr.pantheonsorbonne.service.NotificationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.jboss.logging.Logger;

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
                    // Extraire les détails de la notification
                    String recipientEmail = exchange.getIn().getHeader("userEmail", String.class);
                    Long reservationNumber = exchange.getIn().getHeader("reservationNumber", Long.class);

                    if (recipientEmail != null && reservationNumber != null) {
                        // Appeler le service pour envoyer l'e-mail
                        notificationService.sendNotification(recipientEmail, reservationNumber);
                    } else {
                        LOG.error("Les informations de notification sont incomplètes.");
                    }
                })
                .log("Notification traitée avec succès.");
    }
}
