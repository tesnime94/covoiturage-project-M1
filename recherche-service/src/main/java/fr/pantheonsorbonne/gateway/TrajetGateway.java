package fr.pantheonsorbonne.gateway;

import java.util.List;

import org.apache.camel.ProducerTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.pantheonsorbonne.dto.TrajetDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TrajetGateway {

    @Inject
    private ProducerTemplate producerTemplate;

    @Inject
    private ObjectMapper objectMapper; //  permet de désérialiser le json en objet java pour après le mettre en list

    // Récupération des trajets du service Trajet, par la route getTrajets qui se trouve dans le camel

    public List<TrajetDTO> getTrajets(String villeDepart) {
        try {
            // Envoie une requête par la route getTrajet et récupère la réponse brute en json
            Object response = producerTemplate.requestBody("direct:getTrajets", villeDepart);
            String jsonResponse = response.toString();

            // Désérialise la réponse JSON en List<TrajetDTO>
            return objectMapper.readValue(jsonResponse, new TypeReference<List<TrajetDTO>>() {});
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des trajets : " + e.getMessage(), e);
        }
    }
}

