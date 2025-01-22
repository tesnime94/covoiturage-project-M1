package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dao.TrajetPrincipalDAO;
import fr.pantheonsorbonne.dto.SousTrajetDTO;
import fr.pantheonsorbonne.dto.TrajetAvecSousTrajetsDTO;
import fr.pantheonsorbonne.entity.SousTrajet;
import fr.pantheonsorbonne.entity.TrajetPrincipal;
import fr.pantheonsorbonne.exception.InvalidTrajetDataException;
import fr.pantheonsorbonne.exception.TrajetNotFoundException;
import fr.pantheonsorbonne.gateway.NominatimGateway;
import fr.pantheonsorbonne.gateway.OverpassGateway;
import fr.pantheonsorbonne.gateway.UserGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.apache.camel.ProducerTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@Named("customTrajetService")
@ApplicationScoped
public class TrajetService {

    @Inject
    SousTrajetDAO sousTrajetDAO;

    @Inject
    private ProducerTemplate producerTemplate;

    @Inject
    private TrajetPrincipalDAO trajetPrincipalDAO;

    @Inject
    private UserGateway userGateway;

    @Inject
    NominatimGateway nominatimGateway;

    @Inject
    OverpassGateway overpassGateway;

    LocalDate today = LocalDate.now();
    LocalTime now = LocalTime.now();


    @Transactional
    public TrajetPrincipal createTrajet(String villeDepart, String villeArrivee, String date, String horaire, Integer nombreDePlaces, Double prix, String conducteurMail) {
        // check de l'existence du conducteur
        if (!userGateway.validateUserByMail(conducteurMail)) {
            throw new InvalidTrajetDataException("Le conducteur n'existe pas dans la base de données.");
        }
// Vérifier si le conducteur a déjà un trajet à cette date et cette heure
        if (trajetPrincipalDAO.hasTrajetAtSameTime(conducteurMail, date, horaire)) {
            throw new InvalidTrajetDataException("Le conducteur a déjà un trajet prévu à cette date et cette heure.");
        }
        if (villeDepart == null || villeDepart.isEmpty() || villeArrivee == null || villeArrivee.isEmpty()) {
            throw new InvalidTrajetDataException("Les villes de départ et d'arrivée sont obligatoires.");
        }
        if (prix == null || prix <= 0) {
            throw new InvalidTrajetDataException("Le prix doit être supérieur à 0.");
        }
        if (nombreDePlaces == null || nombreDePlaces <= 0) {
            throw new InvalidTrajetDataException("Le nombre de places doit être supérieur à 0.");
        }
//        if (date == null || (date.isBefore(today))) {
//            throw new InvalidTrajetDataException("La date doit être une date future.");
//        }
//        if (date.equals(today) && (horaire == null || horaire.isBefore(now))) {
//            throw new InvalidTrajetDataException("Si la date est aujourd'hui, l'horaire doit être dans le futur.");
//        }

        // Création de l'objet TrajetPrincipal
        TrajetPrincipal trajetPrincipal = new TrajetPrincipal();
        trajetPrincipal.setVilleDepart(villeDepart);
        trajetPrincipal.setVilleArrivee(villeArrivee);
        trajetPrincipal.setDate(date);
        trajetPrincipal.setHoraire(horaire);
        trajetPrincipal.setNbPlaces(nombreDePlaces);
        trajetPrincipal.setPrix(prix);
        trajetPrincipal.setConducteurMail(conducteurMail);

        // Appel de l'API pour récupérer les villes intermédiaires
        List<String> grandesVilles = callExternalApiForIntermediateCities(villeDepart, villeArrivee);
        for (String ville : grandesVilles) {
            SousTrajet sousTrajet = new SousTrajet();
            sousTrajet.setVilleDepart(villeDepart); // Point de départ pour ce sous-trajet
            sousTrajet.setVilleArrivee(ville);      // Ville intermédiaire
            sousTrajet.setDate(date);
            sousTrajet.setHoraire(horaire);         // Même horaire que le trajet principal
            sousTrajet.setTrajetPrincipal(trajetPrincipal); // Lier au trajet principal
            trajetPrincipal.getSousTrajets().add(sousTrajet); // Ajouter le sous-trajet au trajet principal
            villeDepart = ville; // Mise à jour du point de départ pour le prochain sous-trajet
        }

        // Sauvegarde du trajet principal avec ses sous-trajets
        trajetPrincipalDAO.save(trajetPrincipal);
        return trajetPrincipal;
    }


    private List<String> callExternalApiForIntermediateCities(String villeDepart, String villeArrivee) {
        // Obtenir les coordonnées des villes via le NominatimGateway
        double[] coordDepart = nominatimGateway.getCoordinates(villeDepart);
        double[] coordArrivee = nominatimGateway.getCoordinates(villeArrivee);

        // Construire la requête Overpass
        String overpassQuery = buildOverpassQuery(coordDepart, coordArrivee);

        // Utiliser le OverpassGateway pour récupérer les villes intermédiaires
        return overpassGateway.fetchIntermediateCities(overpassQuery);
    }


    private String buildOverpassQuery(double[] coordDepart, double[] coordArrivee) {
        double south = Math.min(coordDepart[0], coordArrivee[0]);
        double west = Math.min(coordDepart[1], coordArrivee[1]);
        double north = Math.max(coordDepart[0], coordArrivee[0]);
        double east = Math.max(coordDepart[1], coordArrivee[1]);

        // Conserver la requête en texte brut sans encodage
        return String.format(Locale.US, """
                [out:json];(node["place"="city"](%.6f,%.6f,%.6f,%.6f););out body;
                """, south, west, north, east);
    }


    // Méthode pour récupérer un trajet principal par son ID
    public TrajetPrincipal getTrajetById(Long id) {
        TrajetPrincipal trajet = trajetPrincipalDAO.findById(id);
        if (trajet == null) {
            throw new TrajetNotFoundException("Trajet avec ID " + id + " non trouvé.");
        }
        return trajet;
    }

    public List<TrajetPrincipal> getAllTrajets() {
        return trajetPrincipalDAO.findAll();
    }

    public boolean deleteTrajetById(Long id) {
        return trajetPrincipalDAO.deleteById(id);
    }

    public List<TrajetAvecSousTrajetsDTO> getAllTrajetsWithSousTrajetsDTO() {
        List<TrajetPrincipal> trajets = trajetPrincipalDAO.findAllWithSousTrajets(); // Méthode DAO
        return trajets.stream().map(trajet ->
                new TrajetAvecSousTrajetsDTO(
                        trajet.getId(),
                        trajet.getVilleDepart(),
                        trajet.getVilleArrivee(),
                        trajet.getDate(),
                        trajet.getHoraire(),
                        trajet.getNbPlaces(),
                        trajet.getPrix(),
                        trajet.getConducteurMail(),
                        trajet.getSousTrajets().stream()
                                .map(sousTrajet -> new SousTrajetDTO(
                                        sousTrajet.getId(),
                                        sousTrajet.getVilleDepart(),
                                        sousTrajet.getVilleArrivee(),
                                        sousTrajet.getDate(),
                                        sousTrajet.getHoraire()
                                ))
                                .toList()
                )
        ).toList();
    }

    @Transactional
    public List<TrajetAvecSousTrajetsDTO> getTrajetsWithSousTrajetsByVilleDepart(String villeDepart) {
        List<TrajetPrincipal> trajets = trajetPrincipalDAO.findWithSousTrajetsByVilleDepart(villeDepart);

        return trajets.stream().map(trajet ->
                new TrajetAvecSousTrajetsDTO(
                        trajet.getId(),
                        trajet.getVilleDepart(),
                        trajet.getVilleArrivee(),
                        trajet.getDate(),
                        trajet.getHoraire(),
                        trajet.getNbPlaces(),
                        trajet.getPrix(),
                        trajet.getConducteurMail(),
                        trajet.getSousTrajets().stream()
                                .map(sousTrajet -> new SousTrajetDTO(
                                        sousTrajet.getId(),
                                        sousTrajet.getVilleDepart(),
                                        sousTrajet.getVilleArrivee(),
                                        sousTrajet.getDate(),
                                        sousTrajet.getHoraire()
                                ))
                                .toList()
                )
        ).toList();
    }


}
