package fr.pantheonsorbonne.dao;

import java.util.List;

import fr.pantheonsorbonne.entity.HistoriqueRecherche;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class HistoriqueRechercheDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(HistoriqueRecherche historique) {
        entityManager.persist(historique);
    }

    public HistoriqueRecherche findById(Long id) {
        return entityManager.find(HistoriqueRecherche.class, id);
    }

    public List<HistoriqueRecherche> findAll() {
        return entityManager.createQuery(
            "SELECT h FROM HistoriqueRecherche h", HistoriqueRecherche.class
        ).getResultList();
    }
}

