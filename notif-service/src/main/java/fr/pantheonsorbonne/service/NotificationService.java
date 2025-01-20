package fr.pantheonsorbonne.service;

import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.Mail;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NotificationService {

    @Inject
    Mailer mailer;

    public void sendNotification(String recipientEmail, Long reservationNumber) {
        String subject = "Confirmation de réservation";
        String body = String.format(
                "Bonjour,\n\nVotre réservation n°%d a été confirmée avec succès.\nMerci pour votre confiance.\n\nCordialement,\nL'équipe.",
                reservationNumber
        );

        mailer.send(Mail.withText(recipientEmail, subject, body));
    }
}
