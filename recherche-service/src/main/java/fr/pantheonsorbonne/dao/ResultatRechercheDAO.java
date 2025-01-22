package fr.pantheonsorbonne.dao;

import fr.pantheonsorbonne.entity.ResultatRecherche;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class ResultatRechercheDAO {

    

    @PersistenceContext
    private EntityManager entityManager;
<<<<<<< Updated upstream

=======
    
>>>>>>> Stashed changes
    @Transactional
    public void save(ResultatRecherche result) {
        entityManager.persist(result);
    }

    public ResultatRecherche findById(Long id) {
        return entityManager.find(ResultatRecherche.class, id);
    }
}

