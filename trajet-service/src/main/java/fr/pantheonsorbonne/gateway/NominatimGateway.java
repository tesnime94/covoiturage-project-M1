package fr.pantheonsorbonne.gateway;

import fr.pantheonsorbonne.exception.NominatimApiException;
import fr.pantheonsorbonne.exception.ResponseParsingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.apache.camel.ProducerTemplate;

import java.io.StringReader;

@ApplicationScoped
public class NominatimGateway {

    @Inject
    private ProducerTemplate producerTemplate;

    public double[] getCoordinates(String ville) {
        String response;
        try {
            response = producerTemplate.requestBodyAndHeader(
                    "direct:nominatimSearch",
                    null,
                    "ville",
                    ville,
                    String.class
            );
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

            return new double[]{lat, lon};
        } catch (Exception e) {
            throw new ResponseParsingException("Erreur pendant le parsing des coordonnées pour la ville : " + ville, e);
        }
    }
}
