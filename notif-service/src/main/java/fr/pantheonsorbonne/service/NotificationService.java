package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.NotificationDAO;
import fr.pantheonsorbonne.dto.NotificationDTO;
import fr.pantheonsorbonne.entity.Notification;
import fr.pantheonsorbonne.entity.NotificationType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class NotificationService {

    @Inject
    private NotificationDAO notificationDAO;

    /**
     * Créer une nouvelle notification et la sauvegarder en base de données.
     *
     * @param userId  ID de l'utilisateur
     * @param message Contenu de la notification
     * @param type    Type de notification
     * @return Notification créée
     */
    @Transactional
    public Notification createNotification(String userId, String message, NotificationType type) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setType(type);

        notificationDAO.save(notification);
        return notification;
    }

    /**
     * Récupérer toutes les notifications d'un utilisateur donné.
     *
     * @param userId ID de l'utilisateur
     * @return Liste des notifications sous forme de DTO
     */
    public List<NotificationDTO> getNotificationsByUserId(String userId) {
        List<Notification> notifications = notificationDAO.findByUserId(userId);
        return notifications.stream()
                .map(n -> new NotificationDTO(
                        n.getId(),
                        n.getUserId(),
                        n.getMessage(),
                        n.getType(),
                        n.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
