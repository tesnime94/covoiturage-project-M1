package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.entity.SousTrajet;
import fr.pantheonsorbonne.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SousTrajetService {
    SousTrajetDAO sousTrajetDAO;

    public SousTrajet getSousTrajetById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("L'ID du sous-trajet doit être un entier positif.");
        }

        SousTrajet sousTrajet = sousTrajetDAO.findByIdSousTrajet(id);
        if (sousTrajet == null) {
            throw new ResourceNotFoundException("Sous-trajet non trouvé pour l'ID : " + id);
        }

        return sousTrajet;
    }
}
