package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.HistoriqueRecherche;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class HistoriqueRechercheDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
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

