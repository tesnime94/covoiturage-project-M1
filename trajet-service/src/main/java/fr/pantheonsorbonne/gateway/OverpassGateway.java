package fr.pantheonsorbonne.gateway;

import fr.pantheonsorbonne.exception.OverpassApiException;
import fr.pantheonsorbonne.exception.ResponseParsingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.*;
import org.apache.camel.ProducerTemplate;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class OverpassGateway {

    @Inject
    private ProducerTemplate producerTemplate;

    public List<String> fetchIntermediateCities(String query) {
        String response;
        try {
            response = producerTemplate.requestBodyAndHeader(
                    "direct:fetchIntermediateCities",
                    null,
                    "data",
                    query,
                    String.class
            );
        } catch (Exception e) {
            throw new OverpassApiException("Erreur lors de l'appel à Overpass API : " + e.getMessage(), e);
        }

        return parseOverpassResponse(response);
    }

    private List<String> parseOverpassResponse(String response) {
        List<String> villes = new ArrayList<>();
        if (response == null || response.isEmpty()) {
            throw new ResponseParsingException("La réponse Overpass est nulle ou vide.");
        }

        try (JsonReader reader = Json.createReader(new StringReader(response))) {
            JsonObject jsonResponse = reader.readObject();
            JsonArray elements = jsonResponse.getJsonArray("elements");
            for (JsonValue value : elements) {
                if (value.getValueType() == JsonValue.ValueType.OBJECT) {
                    JsonObject element = value.asJsonObject();
                    JsonObject tags = element.getJsonObject("tags");
                    if (tags != null && tags.containsKey("name")) {
                        villes.add(tags.getString("name"));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseParsingException("Erreur pendant le parsing de la réponse Overpass.", e);
        }
        return villes;
    }
}
