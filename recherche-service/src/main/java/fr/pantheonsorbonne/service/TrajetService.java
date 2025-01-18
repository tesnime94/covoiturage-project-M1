package fr.pantheonsorbonne.service;

import java.util.List;

import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dao.TrajetDAO;
import fr.pantheonsorbonne.dto.SousTrajetDTO;
import fr.pantheonsorbonne.dto.TrajetDTO;
import fr.pantheonsorbonne.entity.SousTrajet;
import fr.pantheonsorbonne.entity.Trajet;
import fr.pantheonsorbonne.gateway.TrajetGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TrajetService {

    @Inject
    private TrajetDAO trajetDAO;

    @Inject
    private SousTrajetDAO sousTrajetDAO;

    @Inject
    private TrajetGateway trajetGateway;

    @Transactional
    public void synchroniserTrajets(String villeDepart) {
        try {
            // Étape 1 : Récupérer les trajets depuis le microservice Trajet
            List<TrajetDTO> trajetsDTO = trajetGateway.getTrajets(villeDepart);

            // Étape 2 : Parcourir et enregistrer chaque trajet principal
            for (TrajetDTO trajetDTO : trajetsDTO) {
                Trajet trajet = new Trajet();
                trajet.setVilleDepart(trajetDTO.villeDepart());
                trajet.setVilleArrivee(trajetDTO.villeArrivee());
                trajet.setDate(trajetDTO.date());
                trajet.setHoraire(trajetDTO.horaire());
                trajet.setNombreDePlaces(trajetDTO.placeDisponible()); // Initialise placeDisponible avec nombreDePlaces
                trajet.setPlaceDisponible(trajetDTO.placeDisponible());
                trajet.setPrix(trajetDTO.prix());
                trajet.setConducteurMail(trajetDTO.conducteurMail());
                trajetDAO.save(trajet);

                // Étape 3 : Enregistrer les sous-trajets associés , on ne récupère pas l'horaire
                if (trajetDTO.sousTrajets() != null) {
                    for (SousTrajetDTO sousTrajetDTO : trajetDTO.sousTrajets()) {
                        SousTrajet sousTrajet = new SousTrajet();
                        sousTrajet.setVilleDepart(sousTrajetDTO.villeDepart());
                        sousTrajet.setVilleArrivee(sousTrajetDTO.villeArrivee());
                        sousTrajet.setDate(sousTrajetDTO.date());
                        sousTrajet.setTrajet(trajet); // On Associe le sous-trajet au trajet principal
                        sousTrajetDAO.save(sousTrajet);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la synchronisation des trajets : " + e.getMessage(), e);
        }
    }

    
}

