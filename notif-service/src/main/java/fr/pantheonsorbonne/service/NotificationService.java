package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.NotificationDAO;
import fr.pantheonsorbonne.entity.Notification;
import fr.pantheonsorbonne.entity.NotificationType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class NotificationService {

    private final NotificationDAO notificationDAO;

    public NotificationService(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    @Transactional
    public void createNotification(String userId, String message, NotificationType type) {
        Notification notification = new Notification();
        notification.userId = userId;
        notification.message = message;
        notification.type = type;
        notification.timestamp = LocalDateTime.now();
        notificationDAO.persist(notification);
    }

    public List<Notification> getNotificationsForUser(String userId) {
        return notificationDAO.find("userId", userId).list();
    }
}
