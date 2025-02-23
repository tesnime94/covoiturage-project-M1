package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.Resa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class ResaDAO {
    @PersistenceContext
    private EntityManager em;

    // Méthode pour enregistrer une réservation
    public void save(Resa resa) {
        em.persist(resa);
    }


    public Resa findById(Long resaNumber) {
        return em.createQuery(
                        "SELECT r FROM Resa r WHERE r.resaNumber = :resaNumber", Resa.class
                )
                .setParameter("resaNumber", resaNumber)
                .getSingleResult();
    }
}
