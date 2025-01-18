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

    // Méthode pour trouver un sous-trajet par ID
    public SousTrajet findByIdSousTrajet(Long id) {
        return em.createQuery(
                        "SELECT st FROM SousTrajet st WHERE st.id = :id", SousTrajet.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<SousTrajet> findByTrajetPrincipalId(Long trajetPrincipalId) {
        return em.createQuery("SELECT s FROM SousTrajet s WHERE s.trajetPrincipal.id = :trajetPrincipalId", SousTrajet.class)
                .setParameter("trajetPrincipalId", trajetPrincipalId)
                .getResultList();
    }

//    public List<SousTrajet> findByVilleDepart(String villeDepart) {
//        return em.createQuery(
//                        "SELECT s FROM SousTrajet s WHERE s.villeDepart = :villeDepart",
//                        SousTrajet.class)
//                .setParameter("villeDepart", villeDepart)
//                .getResultList();
//    }

}
