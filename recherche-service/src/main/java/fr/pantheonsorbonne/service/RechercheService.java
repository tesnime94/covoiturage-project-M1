package fr.pantheonsorbonne.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.pantheonsorbonne.dao.HistoriqueRechercheDAO;
import fr.pantheonsorbonne.dao.RequeteTestDAO;
import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dao.TrajetDAO;
import fr.pantheonsorbonne.dto.RequeteDTO;
import fr.pantheonsorbonne.dto.ResultatDTO;
import fr.pantheonsorbonne.dto.TrajetDTO;
import fr.pantheonsorbonne.entity.HistoriqueRecherche;
import fr.pantheonsorbonne.entity.Trajet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RechercheService {

    @Inject
    private TrajetDAO trajetDAO;

    @Inject
    private SousTrajetDAO sousTrajetDAO;

    @Inject
    private HistoriqueRechercheDAO historiqueRechercheDAO;

    @Inject
    private RequeteTestDAO testDAO;

    
    @Transactional
    public void initialiserDonneesDeTest() {
        testDAO.create();
    }


     @Transactional
    public ResultatDTO rechercherTrajets(RequeteDTO requeteDTO) {
        // Étape 1 : Extraire les critères de recherche
        String villeDepart = requeteDTO.getVilleDepart();
        String villeArrivee = requeteDTO.getVilleArrivee();
        LocalDate date = requeteDTO.getDate();
        LocalTime horaire = requeteDTO.getHoraire();
        Double prix = requeteDTO.getPrix();

        // Étape 2 : Initialiser et enregistrer l'historique de recherche
        HistoriqueRecherche historique = new HistoriqueRecherche();
        historique.setVilleDepart(villeDepart);
        historique.setVilleArrivee(villeArrivee);
        historique.setRechercheEffectueeLe(new Date());
        historiqueRechercheDAO.save(historique);

        // Étape 3 : Recherche des trajets principaux
        List<Trajet> trajetsTrouves = trajetDAO.findTrajetByCriteria(villeDepart, villeArrivee, date, horaire, prix);
        if (!trajetsTrouves.isEmpty()) {
            return new ResultatDTO(true, "Nous avons trouvés des trajets correspondant à votre recherche.",
                    rechercherEtConvertir(trajetsTrouves));
        }

        // Étape 4 : Recherche dans les sous-trajets
        trajetsTrouves = sousTrajetDAO.findSousTrajetByCriteria(villeDepart, villeArrivee, date, horaire, prix);
        if (!trajetsTrouves.isEmpty()) {
            return new ResultatDTO(true,
                    String.format("Nous avons seulement trouvés des trajets qui passent par %s. Nous allons contacter les conducteurs pour vérifier si ils acceptent de vous déposer à Lyon.", villeArrivee),
                    rechercherEtConvertir(trajetsTrouves));
        }

        // Étape 5 : Aucun résultat trouvé
        return new ResultatDTO(false, "Aucun trajet ne correspond à votre recherche.", null);
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
                trajet.getConducteurMail(),
                List.of() // Sous-trajets à ajouter si nécessaire, mais pas besoin de les avoir lors du resultat
        );
    }
}






