package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.SousTrajet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class SousTrajetDAO {
    @PersistenceContext
    private EntityManager em;

    // Méthode pour enregistrer un sous-trajet
    public void save(SousTrajet sousTrajet) {
        em.persist(sousTrajet);
    }

    // Méthode pour trouver un sous-trajet par ID
    public SousTrajet findById(Long id) {
        return em.find(SousTrajet.class, id);
    }

    // Méthode pour supprimer un sous-trajet par ID
    public void deleteById(Long id) {
        SousTrajet sousTrajet = findById(id);
        if (sousTrajet != null) {
            em.remove(sousTrajet);
        }
    }
}
