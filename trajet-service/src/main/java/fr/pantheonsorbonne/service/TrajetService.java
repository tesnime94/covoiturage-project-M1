package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dao.TrajetPrincipalDAO;
import fr.pantheonsorbonne.entity.SousTrajet;
import fr.pantheonsorbonne.entity.TrajetPrincipal;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class TrajetService {
    @Inject
    private TrajetPrincipalDAO trajetPrincipalDAO;

    @Inject
    private SousTrajetDAO sousTrajetDAO;

    // Méthode pour créer un trajet principal et générer des sous-trajets
    @Transactional
    public TrajetPrincipal createTrajet(String villeDepart, String villeArrivee, Date horaire, Double prix, Long conducteurId) {
        // Étape 1 : Créer et sauvegarder le trajet principal
        TrajetPrincipal trajetPrincipal = new TrajetPrincipal();
        trajetPrincipal.setVilleDepart(villeDepart);
        trajetPrincipal.setVilleArrivee(villeArrivee);
        trajetPrincipal.setHoraire(horaire);
        trajetPrincipal.setPrix(prix);
        trajetPrincipal.setConducteurId(conducteurId);

        trajetPrincipalDAO.save(trajetPrincipal);

        // Étape 2 : Appeler une API externe pour trouver les grandes villes intermédiaires
        List<String> grandesVilles = callExternalApiForIntermediateCities(villeDepart, villeArrivee);

        // Étape 3 : Générer les sous-trajets à partir des grandes villes
        List<SousTrajet> sousTrajets = new ArrayList<>();
        for (String ville : grandesVilles) {
            SousTrajet sousTrajet = new SousTrajet();
            sousTrajet.setVilleDepart(villeDepart); // Ville de départ du sous-trajet
            sousTrajet.setVilleArrivee(ville);     // Ville intermédiaire
            sousTrajet.setHoraire(horaire);       // Horaire identique au trajet principal
            sousTrajet.setTrajetPrincipal(trajetPrincipal);

            sousTrajets.add(sousTrajet);
            sousTrajetDAO.save(sousTrajet); // Sauvegarde en base
        }

        // Associer les sous-trajets au trajet principal
        trajetPrincipal.setSousTrajets(sousTrajets);

        return trajetPrincipal;
    }

    // Méthode simulée pour appeler une API externe et obtenir les grandes villes
    private List<String> callExternalApiForIntermediateCities(String villeDepart, String villeArrivee) {
        // Ici, on simule une réponse de l'API
        // Cette méthode devra être remplacée par un appel HTTP réel
        return List.of("Orléans", "Tours", "Poitiers");
    }

    // Méthode pour récupérer un trajet principal par son ID
    public TrajetPrincipal getTrajetById(Long id) {
        return trajetPrincipalDAO.findById(id);
    }
}
