package fr.pantheonsorbonne.gateway;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.dto.TrajetCompletDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.ProducerTemplate;

import java.util.List;

@ApplicationScoped
public class TrajetGateway {

    @Inject
    private ProducerTemplate producerTemplate;

    @Inject
    private ObjectMapper objectMapper; //  permet de désérialiser le json en objet java pour après le mettre en list

    // Récupération des trajets du service Trajet, par la route getTrajets qui se trouve dans le camel

    //    public List<TrajetDTO> getTrajets(String villeDepart) {
//
//        try {
//            // Envoie la ville de départ et récupère la réponse en JSON
//            String jsonResponse = (String) producerTemplate.requestBody("direct:getTrajets", villeDepart);
//
//            // Désérialise la réponse JSON en List<TrajetDTO>
//            return objectMapper.readValue(jsonResponse, new TypeReference<List<TrajetDTO>>() {
//            });
//        } catch (Exception e) {
//            throw new RuntimeException("Erreur lors de la récupération des trajets : " + e.getMessage(), e);
//        }
//    }
    public List<TrajetCompletDTO> getTrajets(String villeDepart) {
        try {
            // Envoie la ville de départ et récupère la réponse
            Object response = producerTemplate.requestBody("direct:getTrajets", villeDepart);

            // Log pour vérifier le type de la réponse
            System.out.println("Type de réponse de la route : " + response.getClass().getName());

            // Si la réponse est une chaîne JSON, désérialisez-la
            if (response instanceof String) {
                String jsonResponse = (String) response;
                return objectMapper.readValue(jsonResponse, new TypeReference<List<TrajetCompletDTO>>() {
                });
            }

            // Si la réponse est une liste de LinkedHashMap
            if (response instanceof List) {
                List<?> rawList = (List<?>) response;
                return rawList.stream()
                        .map(item -> objectMapper.convertValue(item, TrajetCompletDTO.class)) // Convertir chaque élément
                        .toList();
            }

            // Si le type n'est ni String ni List, lancez une exception
            throw new RuntimeException("Type de réponse inattendu : " + response.getClass().getName());

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des trajets : " + e.getMessage(), e);
        }
    }


}

