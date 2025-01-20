package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.Notification;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class NotificationDAO {

    @PersistenceContext
    private EntityManager em;

    /**
     * Sauvegarder une notification en base de données.
     *
     * @param notification l'entité Notification à sauvegarder
     */
    public void save(Notification notification) {
        em.persist(notification);
    }

    /**
     * Trouver une notification par son ID.
     *
     * @param id l'identifiant de la notification
     * @return Notification si elle est trouvée
     */
    public Notification findById(Long id) {
        return em.find(Notification.class, id);
    }

    /**
     * Trouver toutes les notifications pour un utilisateur donné.
     *
     * @param userId l'ID de l'utilisateur
     * @return Liste des notifications
     */
    public List<Notification> findByUserId(String userId) {
        return em.createQuery(
                        "SELECT n FROM Notification n WHERE n.userId = :userId ORDER BY n.createdAt DESC",
                        Notification.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }
}
