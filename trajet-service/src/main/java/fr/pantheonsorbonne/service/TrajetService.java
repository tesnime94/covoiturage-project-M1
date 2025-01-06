package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dao.TrajetPrincipalDAO;
import fr.pantheonsorbonne.entity.SousTrajet;
import fr.pantheonsorbonne.entity.TrajetPrincipal;
import fr.pantheonsorbonne.exception.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.*;
import jakarta.transaction.Transactional;
import org.apache.camel.ProducerTemplate;

import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class TrajetService {

    @Inject
    SousTrajetDAO sousTrajetDAO;
    @Inject
    private ProducerTemplate producerTemplate;
    @Inject
    private TrajetPrincipalDAO trajetPrincipalDAO;

    LocalDate today = LocalDate.now();
    LocalTime now = LocalTime.now();


    @Transactional
    public TrajetPrincipal createTrajet(String villeDepart, String villeArrivee, LocalDate date, LocalTime horaire, Integer nombreDePlaces, Double prix, Long conducteurId) {
        // Validation des données d'entrée
        if (villeDepart == null || villeDepart.isEmpty() || villeArrivee == null || villeArrivee.isEmpty()) {
            throw new InvalidTrajetDataException("Les villes de départ et d'arrivée sont obligatoires.");
        }
        if (prix == null || prix <= 0) {
            throw new InvalidTrajetDataException("Le prix doit être supérieur à 0.");
        }
        if (conducteurId == null || conducteurId <= 0) {
            throw new InvalidTrajetDataException("Un conducteur valide est obligatoire.");
        }
        if (nombreDePlaces == null || nombreDePlaces <= 0) {
            throw new InvalidTrajetDataException("Le nombre de places doit être supérieur à 0.");
        }
        if (date == null || (date.isBefore(today))) {
            throw new InvalidTrajetDataException("La date doit être une date future.");
        }
        if (date.equals(today) && (horaire == null || horaire.isBefore(now))) {
            throw new InvalidTrajetDataException("Si la date est aujourd'hui, l'horaire doit être dans le futur.");
        }

        // Création de l'objet TrajetPrincipal
        TrajetPrincipal trajetPrincipal = new TrajetPrincipal();
        trajetPrincipal.setVilleDepart(villeDepart);
        trajetPrincipal.setVilleArrivee(villeArrivee);
        trajetPrincipal.setDate(date);
        trajetPrincipal.setHoraire(horaire);
        trajetPrincipal.setNbPlaces(nombreDePlaces);
        trajetPrincipal.setPrix(prix);
        trajetPrincipal.setConducteurId(conducteurId);

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
        String overpassQuery = buildOverpassQuery(villeDepart, villeArrivee);
        String encodedQuery = URLEncoder.encode(overpassQuery, StandardCharsets.UTF_8);
        System.out.println("Requête Overpass encodée : " + encodedQuery);

        String response;
        try {
            response = producerTemplate.requestBodyAndHeader(
                    "direct:fetchIntermediateCities",
                    null,
                    "data",
                    overpassQuery,
                    String.class
            );
            System.out.println("Overpass Response: " + response);
        } catch (Exception e) {
            throw new OverpassApiException("Erreur lors de l'appel à Overpass API : " + e.getMessage(), e);
        }

        return parseOverpassResponse(response);
    }


    private String buildOverpassQuery(String villeDepart, String villeArrivee) {
        double[] coordDepart = getCoordinates(villeDepart);
        double[] coordArrivee = getCoordinates(villeArrivee);

        System.out.printf("Coordonnées de %s : lat=%.6f, lon=%.6f%n", villeDepart, coordDepart[0], coordDepart[1]);
        System.out.printf("Coordonnées de %s : lat=%.6f, lon=%.6f%n", villeArrivee, coordArrivee[0], coordArrivee[1]);

        double south = Math.min(coordDepart[0], coordArrivee[0]);
        double west = Math.min(coordDepart[1], coordArrivee[1]);
        double north = Math.max(coordDepart[0], coordArrivee[0]);
        double east = Math.max(coordDepart[1], coordArrivee[1]);

        // Conserver la requête en texte brut sans encodage
        return String.format(Locale.US, """
                [out:json];(node["place"="city"](%.6f,%.6f,%.6f,%.6f););out body;
                """, south, west, north, east);
    }

    private double[] getCoordinates(String ville) {
        String response;
        try {
            response = producerTemplate.requestBodyAndHeader(
                    "direct:nominatimSearch",
                    null,
                    "ville",
                    ville,
                    String.class
            );
            System.out.println("Réponse brute de Nominatim : " + response);
        } catch (Exception e) {
            throw new NominatimApiException("Erreur lors de l'appel à Nominatim API pour la ville : " + ville, e);
        }

        try (JsonReader reader = Json.createReader(new StringReader(response))) {
            JsonArray jsonArray = reader.readArray();
            if (jsonArray.isEmpty()) {
                throw new NominatimApiException("Aucun résultat trouvé pour la ville : " + ville);
            }

            JsonObject jsonResponse = jsonArray.getJsonObject(0);
            double lat = Double.parseDouble(jsonResponse.getString("lat"));
            double lon = Double.parseDouble(jsonResponse.getString("lon"));

            System.out.println("Latitude : " + lat + ", Longitude : " + lon);
            return new double[]{lat, lon};
        } catch (Exception e) {
            throw new ResponseParsingException("Erreur pendant le parsing des coordonnées pour la ville : " + ville, e);
        }
    }


    private List<String> parseOverpassResponse(String response) {
        List<String> villes = new ArrayList<>();
        System.out.println("Début du parsing de la réponse Overpass");
        System.out.println("Réponse brute : " + response);

        if (response == null || response.isEmpty()) {
            throw new ResponseParsingException("La réponse Overpass est nulle ou vide.");
        }

        try (JsonReader reader = Json.createReader(new StringReader(response))) {
            JsonObject jsonResponse = reader.readObject();
            if (!jsonResponse.containsKey("elements")) {
                throw new ResponseParsingException("La réponse Overpass ne contient pas de clé 'elements'.");
            }

            JsonArray elements = jsonResponse.getJsonArray("elements");
            for (JsonValue value : elements) {
                if (value.getValueType() == JsonValue.ValueType.OBJECT) {
                    JsonObject element = value.asJsonObject();
                    JsonObject tags = element.getJsonObject("tags");
                    if (tags != null && tags.containsKey("name")) {
                        String ville = tags.getString("name");
                        villes.add(ville);
                        System.out.println("Ville trouvée : " + ville);
                    } else {
                        System.out.println("Le champ 'name' est absent dans 'tags' : " + tags);
                    }
                } else {
                    System.out.println("Un élément non-objet trouvé dans 'elements'. Ignoré.");
                }
            }
        } catch (Exception e) {
            throw new ResponseParsingException("Erreur pendant le parsing de la réponse Overpass.", e);
        }

        System.out.println("Villes extraites : " + villes);
        return villes;
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


}
