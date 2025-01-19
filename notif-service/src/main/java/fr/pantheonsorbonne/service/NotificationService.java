package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.NotificationDAO;
import fr.pantheonsorbonne.entity.Notification;
import fr.pantheonsorbonne.entity.NotificationType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class NotificationService {

    private final NotificationDAO notificationDAO;

    @Inject
    @Channel("notifications") // Channel Kafka configuré dans application.properties
    Emitter<String> notificationEmitter;

    public NotificationService(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    @Transactional
    public void createNotification(String userId, String message, NotificationType type) {
        // Sauvegarde dans la base de données
        Notification notification = new Notification();
        notification.userId = userId;
        notification.message = message;
        notification.type = type;
        notification.timestamp = LocalDateTime.now();
        notificationDAO.persist(notification);

        // Envoi de l'événement au topic Kafka
        String event = String.format("Notification for user %s: %s", userId, message);
        notificationEmitter.send(event);
    }

    public List<Notification> getNotificationsForUser(String userId) {
        return notificationDAO.find("userId", userId).list();
    }
}
