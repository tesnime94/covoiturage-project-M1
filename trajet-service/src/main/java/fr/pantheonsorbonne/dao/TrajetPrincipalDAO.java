package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.TrajetPrincipal;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class TrajetPrincipalDAO {
    @PersistenceContext
    private EntityManager em;

    // Méthode pour enregistrer un trajet principal
    public void save(TrajetPrincipal trajetPrincipal) {
        em.persist(trajetPrincipal);
    }

    // Méthode pour récupérer un trajet principal par ID
    public TrajetPrincipal findById(Long id) {
        return em.find(TrajetPrincipal.class, id);
    }

    // Méthode pour récupérer tous les trajets principaux
    public List<TrajetPrincipal> findAll() {
        return em.createQuery("SELECT t FROM TrajetPrincipal t", TrajetPrincipal.class).getResultList();
    }

    // Méthode pour supprimer un trajet principal par ID
    public void deleteById(Long id) {
        TrajetPrincipal trajetPrincipal = findById(id);
        if (trajetPrincipal != null) {
            em.remove(trajetPrincipal);
        }
    }
}
