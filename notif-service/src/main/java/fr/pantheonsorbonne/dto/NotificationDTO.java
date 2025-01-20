package fr.pantheonsorbonne.dto;

import fr.pantheonsorbonne.entity.NotificationType;

import java.time.LocalDateTime;

public class NotificationDTO {

    private Long id;
    private String userId;
    private String message;
    private NotificationType type;
    private LocalDateTime createdAt;

    public NotificationDTO(Long id, String userId, String message, NotificationType type, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
    }

    // Getters uniquement (immutabilité)
    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
