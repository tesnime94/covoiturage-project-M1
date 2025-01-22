package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.Trajet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;


@ApplicationScoped
public class TrajetDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void save(Trajet trajet) {
        entityManager.persist(trajet);
    }


    public Trajet findById(Long id) {
        return entityManager.find(Trajet.class, id);
    }

    public List<Trajet> findTrajetByCriteria(String villeDepart, String villeArrivee, String date, String horaire, Double prix) {
        String request = "SELECT t FROM Trajet t " +
                "WHERE t.villeDepart = :villeDepart " +
                "AND t.villeArrivee = :villeArrivee " +
                "AND t.date = :date " +
                "AND t.horaire = :horaire " +
                "AND t.prix <= :prix";

        return entityManager.createQuery(request, Trajet.class)
                .setParameter("villeDepart", villeDepart)
                .setParameter("villeArrivee", villeArrivee)
                .setParameter("date", date)
                .setParameter("horaire", horaire)
                .setParameter("prix", prix)
                .getResultList();

    }

    public List<Trajet> findByVilleDepartAndVilleArrivee(String villeDepart, String villeArrivee) {
        return entityManager.createQuery(
                        "SELECT t FROM Trajet t WHERE t.villeDepart = :villeDepart AND t.villeArrivee = :villeArrivee", Trajet.class)
                .setParameter("villeDepart", villeDepart)
                .setParameter("villeArrivee", villeArrivee)
                .getResultList();
    }


    public List<Trajet> findAll() {
        return entityManager.createQuery("SELECT t FROM Trajet t", Trajet.class).getResultList();
    }

    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Trajet").executeUpdate();
    }


}

