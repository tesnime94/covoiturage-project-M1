package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dao.TrajetPrincipalDAO;
import fr.pantheonsorbonne.entity.SousTrajet;
import fr.pantheonsorbonne.entity.TrajetPrincipal;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.*;
import jakarta.transaction.Transactional;
import org.apache.camel.ProducerTemplate;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.apache.camel.model.rest.RestParamType.query;

@ApplicationScoped
public class TrajetService {

    @Inject
    private ProducerTemplate producerTemplate;
    @Inject
    private TrajetPrincipalDAO trajetPrincipalDAO;

    @Inject
    private SousTrajetDAO sousTrajetDAO;

    @Transactional
    public TrajetPrincipal createTrajet(String villeDepart, String villeArrivee, Date horaire, Double prix, Long conducteurId) {
        TrajetPrincipal trajetPrincipal = new TrajetPrincipal();
        trajetPrincipal.setVilleDepart(villeDepart);
        trajetPrincipal.setVilleArrivee(villeArrivee);
        trajetPrincipal.setHoraire(horaire);
        trajetPrincipal.setPrix(prix);
        trajetPrincipal.setConducteurId(conducteurId);

        // Récupérer les villes intermédiaires

        List<String> grandesVilles = callExternalApiForIntermediateCities(villeDepart, villeArrivee);
        for (String ville : grandesVilles) {
            SousTrajet sousTrajet = new SousTrajet();
            sousTrajet.setVilleDepart(villeDepart);
            sousTrajet.setVilleArrivee(ville);
            sousTrajet.setHoraire(horaire);
            sousTrajet.setTrajetPrincipal(trajetPrincipal);
            trajetPrincipal.getSousTrajets().add(sousTrajet);
        }

        trajetPrincipalDAO.save(trajetPrincipal);
        return trajetPrincipal;
    }



    private List<String> callExternalApiForIntermediateCities(String villeDepart, String villeArrivee) {
        String overpassQuery = buildOverpassQuery(villeDepart, villeArrivee);
        String encodedQuery = URLEncoder.encode(overpassQuery, StandardCharsets.UTF_8);
        System.out.println("Requête Overpass encdoée : " + encodedQuery);
        String response = null;
        try{
        // Appeler Overpass API via Camel
        response = producerTemplate.requestBodyAndHeader(
                "direct:fetchIntermediateCities",
                null,
                "data",
                overpassQuery,
                String.class
        );

        System.out.println("Overpass Response: " + response);
        }

        catch (Exception e) {
            System.out.println(" Erreur lors de l'appel à Overpass: "+ e.getMessage());
            e.printStackTrace();
        }


        // Parse la réponse pour extraire les villes
        return parseOverpassResponse(response);
    }

    private double[] getCoordinates(String ville) {
        // Appel à API pour obtenir les coordonnées
        String response = producerTemplate.requestBodyAndHeader(
                "direct:nominatimSearch",
                null,
                "ville",
                ville,
                String.class
        );
        System.out.println("Réponse brute de Nominatim : " + response);
// Parse la réponse JSON pour extraire les coordonnées
        JsonReader reader = Json.createReader(new StringReader(response));
        JsonArray jsonArray = reader.readArray(); // Lire la réponse comme tableau JSON
        reader.close();

        if (jsonArray.isEmpty()) {
            throw new RuntimeException("Aucun résultat trouvé pour la ville : " + ville);
        }

        JsonObject jsonResponse = jsonArray.getJsonObject(0); // Prendre le premier résultat

        // Extraire les coordonnées en tant que chaînes et les convertir en double
        double lat = Double.parseDouble(jsonResponse.getString("lat"));
        double lon = Double.parseDouble(jsonResponse.getString("lon"));

        System.out.println("Latitude : " + lat + ", Longitude : " + lon);


        return new double[]{lat, lon};
    }

    private List<String> parseOverpassResponse(String response) {
        List<String> villes = new ArrayList<>();
        System.out.println("Début du parsing de la réponse Overpass");
        System.out.println("Réponse brute : " + response);

        if (response == null || response.isEmpty()) {
            System.out.println("La réponse est nulle ou vide.");
            return villes;
        }

        try {
            // Lire la réponse JSON
            JsonReader reader = Json.createReader(new StringReader(response));
            JsonObject jsonResponse = reader.readObject();
            reader.close();

            // Vérifier l'existence de la clé "elements"
            if (!jsonResponse.containsKey("elements")) {
                System.out.println("La clé 'elements' est absente dans la réponse.");
                return villes;
            }

            // Parcourir l'array "elements"
            JsonArray elements = jsonResponse.getJsonArray("elements");
            for (JsonValue value : elements) {
                if (value.getValueType() == JsonValue.ValueType.OBJECT) {
                    JsonObject element = value.asJsonObject();

                    // Vérifier l'existence de "tags" et "name"
                    if (element.containsKey("tags")) {
                        JsonObject tags = element.getJsonObject("tags");
                        if (tags.containsKey("name")) {
                            String ville = tags.getString("name");
                            villes.add(ville);
                            System.out.println("Ville trouvée : " + ville);
                        } else {
                            System.out.println("Le champ 'name' est absent dans 'tags' : " + tags);
                        }
                    } else {
                        System.out.println("Le champ 'tags' est absent dans un élément.");
                    }
                } else {
                    System.out.println("Un élément non-objet trouvé dans 'elements'. Ignoré.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur pendant le parsing JSON : " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Villes extraites : " + villes);
        return villes;
    }




    // Méthode pour récupérer un trajet principal par son ID
    public TrajetPrincipal getTrajetById(Long id) {
        return trajetPrincipalDAO.findById(id);
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
        return String.format(Locale.US,"""
[out:json];(node["place"="city"](%.6f,%.6f,%.6f,%.6f););out body;
    """, south, west, north, east);
    }


}
