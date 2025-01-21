package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.SousTrajet;
import fr.pantheonsorbonne.entity.Trajet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class SousTrajetDAO {

    @PersistenceContext
    private EntityManager entityManager;

    // Méthode pour sauvegarder un sous-trajet
    @Transactional
    public void save(SousTrajet sousTrajet) {
        entityManager.persist(sousTrajet);
    }


    public List<Trajet> findSousTrajetByCriteria(String villeDepart, String villeArrivee, LocalDate date, LocalTime horaire, Double prix) {
        String request = "SELECT DISTINCT t " +
                "FROM Trajet t JOIN t.sousTrajets st " +
                "WHERE t.villeDepart = :villeDepart " +
                "AND t.date = :date " +
                "AND t.horaire = :horaire " +
                "AND t.prix <= :prix " +
                "AND st.villeArrivee = :villeArrivee";

        return entityManager.createQuery(request, Trajet.class)
                .setParameter("villeDepart", villeDepart)
                .setParameter("villeArrivee", villeArrivee)
                .setParameter("date", date)
                .setParameter("horaire", horaire)
                .setParameter("prix", prix)
                .getResultList();

    }


// Méthodes qui prennnent en compte que les critères ville de départ et ville d'arrivée

    // Méthode pour rechercher des sous-trajets par ville de départ et d'arrivée
    public List<SousTrajet> findByVilleDepartAndVilleArrivee(String villeDepart, String villeArrivee) {
        return entityManager.createQuery(
                        "SELECT st FROM SousTrajet st WHERE st.villeDepart = :villeDepart AND st.villeArrivee = :villeArrivee", SousTrajet.class)
                .setParameter("villeDepart", villeDepart)
                .setParameter("villeArrivee", villeArrivee)
                .getResultList();
    }

    // Méthode pour trouver les trajets principaux dont les sous-trajets passent par une ville donnée
    public List<Trajet> findTrajetsByVilleArriveeInSousTrajet(String villeDepart, String villeArrivee) {
        return entityManager.createQuery(
                        "SELECT DISTINCT t " +
                                "FROM Trajet t " +
                                "JOIN t.sousTrajets st " +
                                "WHERE t.villeDepart = :villeDepart AND st.villeArrivee = :villeArrivee", Trajet.class)
                .setParameter("villeDepart", villeDepart)
                .setParameter("villeArrivee", villeArrivee)
                .getResultList();
    }


    public void deleteAll() {
        entityManager.createQuery("DELETE FROM SousTrajet").executeUpdate();
    }

}

