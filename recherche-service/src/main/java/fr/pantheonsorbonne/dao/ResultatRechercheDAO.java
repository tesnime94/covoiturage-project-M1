package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.ResultatRecherche;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@ApplicationScoped
public class ResultatRechercheDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(ResultatRecherche result) {
        entityManager.persist(result);
    }

    public ResultatRecherche findById(Long id) {
        return entityManager.find(ResultatRecherche.class, id);
    }
}

