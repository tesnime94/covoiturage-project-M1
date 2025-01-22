package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.HistoriqueRechercheDAO;
import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dao.TrajetDAO;
import fr.pantheonsorbonne.dto.RequeteDTO;
import fr.pantheonsorbonne.dto.ResultatDTO;
import fr.pantheonsorbonne.dto.TrajetDTO;
import fr.pantheonsorbonne.entity.HistoriqueRecherche;
import fr.pantheonsorbonne.entity.Trajet;
import fr.pantheonsorbonne.gateway.ReservationGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class RechercheService {

    @Inject
    private TrajetDAO trajetDAO;

    @Inject
    private SousTrajetDAO sousTrajetDAO;

    @Inject
    private HistoriqueRechercheDAO historiqueRechercheDAO;


    @Inject
    private ReservationGateway reservationGateway;
    @Inject
    private TrajetService trajetService;


    /*@Transactional
    public void initialiserDonneesDeTest() {
        testDAO.create();
    }
        */


    @Transactional
    public ResultatDTO rechercherTrajets(RequeteDTO requeteDTO) {
        //  Extraire les critères de recherche
        String villeDepart = requeteDTO.villeDepart();
        String villeArrivee = requeteDTO.villeArrivee();
        String date = requeteDTO.date();
        String horaire = requeteDTO.horaire();
        Double prix = requeteDTO.prix();

        // Récupération des trajets depuis VilleDepart dans le microservice trajet

        trajetService.synchroniserTrajets(villeDepart);

        //  Initialiser et enregistrer l'historique de recherche
        HistoriqueRecherche historique = new HistoriqueRecherche();
        historique.setVilleDepart(villeDepart);
        historique.setVilleArrivee(villeArrivee);
        historique.setRechercheEffectueeLe(new Date());
        historiqueRechercheDAO.save(historique);

        // Étape 1 : Recherche des trajets principaux
        List<Trajet> trajetsTrouves = trajetDAO.findTrajetByCriteria(villeDepart, villeArrivee, date, horaire, prix);
        if (!trajetsTrouves.isEmpty()) {
            // On enregsitre  le résultat pour l'envoyer à reservation
            ResultatDTO resultat = new ResultatDTO(true, true, "Nous avons trouvés un ou des trajets correspondant à votre recherche", rechercherEtConvertir(trajetsTrouves));
            reservationGateway.envoyerResultatReservation(resultat);
            return resultat;
        }

        // Étape 2 : Recherche dans les sous-trajets Si pas de trajet principaux
        trajetsTrouves = sousTrajetDAO.findSousTrajetByCriteria(villeDepart, villeArrivee, date, horaire, prix);
        if (!trajetsTrouves.isEmpty()) {

            // On enregsitre  le résultat pour l'envoyer à reservation
            ResultatDTO resultat = new ResultatDTO(true, false, String.format("Nous avons seulement trouvés des trajets qui passent par %s. Nous allons contacter les conducteurs pour vérifier si ils acceptent de vous déposer à %s.", villeArrivee, villeArrivee), rechercherEtConvertir(trajetsTrouves));
            reservationGateway.envoyerResultatReservation(resultat);
            return resultat;
        }

        // Étape 3 : Aucun résultat trouvé
        return new ResultatDTO(false, false, "Aucun trajet ne correspond à votre recherche.", null);
    }

    // Méthode pour convertir une liste de trajets en DTO
    private List<TrajetDTO> rechercherEtConvertir(List<Trajet> trajets) {
        List<TrajetDTO> trajetsDTO = new ArrayList<>();
        for (Trajet trajet : trajets) {
            trajetsDTO.add(convertirTrajetEnDTO(trajet));
        }
        return trajetsDTO;
    }

    // Méthode pour convertir un trajet en DTO
    private TrajetDTO convertirTrajetEnDTO(Trajet trajet) {
        return new TrajetDTO(
                trajet.getId(),
                trajet.getVilleDepart(),
                trajet.getVilleArrivee(),
                trajet.getDate(),
                trajet.getHoraire(),
                trajet.getPlaceDisponible(),
                trajet.getPrix(),
                trajet.getConducteurMail()

        );
    }
}






