package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dao.TrajetDAO;
import fr.pantheonsorbonne.dto.SousTrajetDTO;
import fr.pantheonsorbonne.dto.TrajetCompletDTO;
import fr.pantheonsorbonne.entity.SousTrajet;
import fr.pantheonsorbonne.entity.Trajet;
import fr.pantheonsorbonne.gateway.TrajetGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class TrajetService {

    private static final Logger log = LoggerFactory.getLogger(TrajetService.class);
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
            List<TrajetCompletDTO> trajetsDTO = trajetGateway.getTrajets(villeDepart);

            // Étape 2 : Parcourir et enregistrer chaque trajet principal
            for (TrajetCompletDTO trajetDTO : trajetsDTO) {
                System.out.println("TETFETFVDGEVBFGDXRB" + trajetDTO);
                Trajet trajet = new Trajet();
                trajet.setVilleDepart(trajetDTO.villeDepart());
                trajet.setVilleArrivee(trajetDTO.villeArrivee());
                trajet.setDate(trajetDTO.date());
                trajet.setHoraire(trajetDTO.horaire());
                trajet.setNombreDePlaces(trajetDTO.nombreDePlaces()); // Initialise placeDisponible avec nombreDePlaces
                trajet.setPlaceDisponible(trajetDTO.nombreDePlaces());
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
            System.err.println("Erreur capturée : " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace(); // Pour voir toute la pile d'exécution
            throw new RuntimeException("Erreur lors de la synchronisation des trajets : " + e.getMessage(), e);

        }
    }


}

