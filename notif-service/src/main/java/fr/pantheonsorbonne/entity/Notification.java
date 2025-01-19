package fr.pantheonsorbonne.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications") // Nom de la table dans la base de données
public class Notification extends PanacheEntity {

    public String userId; // Identifiant de l'utilisateur cible
    public String message; // Contenu de la notification
    public LocalDateTime timestamp; // Date et heure de la notification

    @Enumerated(EnumType.STRING) // Enregistre l'énumération sous forme de texte
    public NotificationType type; // Type de la notification (confirmation, rappel, etc.)
}
