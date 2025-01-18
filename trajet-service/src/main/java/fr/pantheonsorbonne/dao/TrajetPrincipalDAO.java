package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.TrajetPrincipal;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.time.LocalTime;
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
        return em.createQuery(
                        "SELECT t FROM TrajetPrincipal t WHERE t.id = :id", TrajetPrincipal.class
                )
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<TrajetPrincipal> findByVilleDepart(String villeDepart) {
        return em.createQuery(
                        "SELECT t FROM TrajetPrincipal t WHERE t.villeDepart = :villeDepart",
                        TrajetPrincipal.class)
                .setParameter("villeDepart", villeDepart)
                .getResultList();
    }


    // Méthode pour récupérer tous les trajets principaux
    public List<TrajetPrincipal> findAll() {
        return em.createQuery("SELECT t FROM TrajetPrincipal t", TrajetPrincipal.class).getResultList();
    }

    // Méthode pour supprimer un trajet principal par ID
    public boolean deleteById(Long id) {
        TrajetPrincipal trajet = em.find(TrajetPrincipal.class, id);
        if (trajet != null) {
            em.remove(trajet);
            return true;
        }
        return false;
    }

    public List<TrajetPrincipal> findAllWithSousTrajets() {
        return em.createQuery(
                        "SELECT t FROM TrajetPrincipal t LEFT JOIN FETCH t.sousTrajets",
                        TrajetPrincipal.class)
                .getResultList();
    }

    public boolean hasTrajetAtSameTime(String emailConducteur, LocalDate date, LocalTime horaire) {
        String query = """
                SELECT COUNT(tp)
                FROM TrajetPrincipal tp
                WHERE tp.conducteurMail = :emailConducteur
                AND tp.date = :date
                AND tp.horaire = :horaire
                """;

        Long count = em.createQuery(query, Long.class)
                .setParameter("emailConducteur", emailConducteur)
                .setParameter("date", date)
                .setParameter("horaire", horaire)
                .getSingleResult();

        return count > 0;
    }

    public List<TrajetPrincipal> findWithSousTrajetsByVilleDepart(String villeDepart) {
        return em.createQuery(
                        "SELECT t FROM TrajetPrincipal t JOIN t.sousTrajets st WHERE t.villeDepart = :villeDepart",
                        TrajetPrincipal.class
                )
                .setParameter("villeDepart", villeDepart)
                .getResultList();
    }


}
