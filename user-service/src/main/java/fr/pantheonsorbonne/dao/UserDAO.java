package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserDAO {
    @Inject
    EntityManager em;

    public boolean isUserPresent(String userEmail) {
        return !em.createQuery("SELECT u from User u WHERE u.email = :email")
                .setParameter("email", userEmail)
                .getResultList().isEmpty();
    }

    public void saveUser(User user) {
        em.persist(user);
    }

    @Transactional
    public User getById(Long id) {
        return em.find(User.class, id);
    }

    public User getUserByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Renvoie null si l'utilisateur n'existe pas
        }
    }
}
