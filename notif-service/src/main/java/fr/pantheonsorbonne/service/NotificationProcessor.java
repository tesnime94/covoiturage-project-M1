package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dto.NotificationDTO;
import fr.pantheonsorbonne.entity.Notification;
import fr.pantheonsorbonne.entity.NotificationType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NotificationProcessor {

    @Inject
    private NotificationService notificationService;

    public void process(String message) {
        // Exemple de traitement simple : décomposer le message en JSON et sauvegarder
        String[] parts = message.split(",");
        String userId = parts[0];
        String notificationMessage = parts[1];
        NotificationType type = NotificationType.valueOf(parts[2]);

        // Sauvegarder la notification
        notificationService.createNotification(userId, notificationMessage, type);
    }
}
