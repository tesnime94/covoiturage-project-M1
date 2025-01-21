package fr.pantheonsorbonne.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
//import org.jboss.logging.Logger;

@ApplicationScoped
public class NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);


    //private static final Logger LOG = Logger.getLogger(NotificationService.class);

    @Inject
    Mailer mailer;

    public void sendNotification(String recipientEmail, Long reservationNumber) {
        String subject = "Confirmation de réservation";
        String body = String.format(
                "Bonjour,\n\nVotre réservation n°%d a été confirmée avec succès.\nMerci pour votre confiance.\n\nCordialement,\nL'équipe.",
                reservationNumber
        );

        LOG.info("Tentative d'envoi d'un email...");
        LOG.info("Destinataire : {}", recipientEmail);
        LOG.info("Sujet : {}", subject);
        LOG.info("Corps du message : {}", body);

        try {
            mailer.send(Mail.withText(recipientEmail, subject, body));
            LOG.info("Email envoyé avec succès à {}", recipientEmail);
        } catch (Exception e) {
            LOG.error("Erreur lors de l'envoi de l'email : {}", e.getMessage());
        }
    }

    public void testMailer() {
        String subject = "Test Email";
        String body = "Ceci est un test de l'envoi d'email.";
        String recipient = "ngbalexis94@gmail.com";

        LOG.info("Tentative d'envoi de l'email de test...");
        try {
            mailer.send(Mail.withText(recipient, subject, body));
            LOG.info("Email de test envoyé avec succès à {}", recipient);
        } catch (Exception e) {
            LOG.error("Erreur lors de l'envoi de l'email de test : {}", e.getMessage());
        }
    }

}
