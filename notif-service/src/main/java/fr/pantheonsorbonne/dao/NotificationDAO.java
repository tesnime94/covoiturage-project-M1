package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.Notification;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationDAO implements PanacheRepository<Notification> {
    // Toutes les méthodes CRUD (Create, Read, Update, Delete) sont disponibles via PanacheRepository
}
