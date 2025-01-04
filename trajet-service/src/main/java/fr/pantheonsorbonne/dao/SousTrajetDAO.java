package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.SousTrajet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class SousTrajetDAO {
    @PersistenceContext
    private EntityManager em;

    // Méthode pour enregistrer un sous-trajet
    public void saveSousTrajet(SousTrajet sousTrajet) {
        em.persist(sousTrajet);
    }

    // Méthode pour trouver un sous-trajet par ID
    public SousTrajet findByIdSousTrajet(Long id) {
        return em.find(SousTrajet.class, id);
    }

    // Méthode pour supprimer un sous-trajet par ID
    public void deleteById(Long id) {
        SousTrajet sousTrajet = findByIdSousTrajet(id);
        if (sousTrajet != null) {
            em.remove(sousTrajet);
        }
    }

    public List<SousTrajet> findByTrajetPrincipalId(Long trajetPrincipalId) {
        return em.createQuery("SELECT s FROM SousTrajet s WHERE s.trajetPrincipal.id = :trajetPrincipalId", SousTrajet.class)
                .setParameter("trajetPrincipalId", trajetPrincipalId)
                .getResultList();
    }



}
