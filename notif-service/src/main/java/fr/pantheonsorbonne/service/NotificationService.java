package fr.pantheonsorbonne.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class NotificationService {

    private static final Logger LOG = Logger.getLogger(NotificationService.class);

    @Inject
    Mailer mailer;

    public void sendNotification(String recipientEmail, Long reservationNumber) {
        String subject = "Confirmation de réservation";
        String body = String.format(
                "Bonjour,\n\nVotre réservation n°%d a été confirmée avec succès.\nMerci pour votre confiance.\n\nCordialement,\nL'équipe.",
                reservationNumber
        );

        try {
            mailer.send(Mail.withText(recipientEmail, subject, body));
            LOG.infof("Email envoyé avec succès à %s pour la réservation %d", recipientEmail, reservationNumber);
        } catch (Exception e) {
            LOG.error("Erreur lors de l'envoi de l'email : ", e);
        }
    }
}
