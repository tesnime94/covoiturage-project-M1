package fr.pantheonsorbonne.gateway;

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


    public List<TrajetCompletDTO> getTrajets(String villeDepart) {
        try {
            // Envoie la ville de départ et récupère la réponse
            Object response = producerTemplate.requestBody("direct:getTrajets", villeDepart);

            // Log pour vérifier le type de la réponse
            System.out.println("Type de réponse de la route : " + response.getClass().getName());


            // Si la réponse est une liste de LinkedHashMap

            List<?> rawList = (List<?>) response;
            return rawList.stream()
                    .map(item -> objectMapper.convertValue(item, TrajetCompletDTO.class)) // Convertir chaque élément
                    .toList();


        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des trajets : " + e.getMessage(), e);
        }
    }
}

