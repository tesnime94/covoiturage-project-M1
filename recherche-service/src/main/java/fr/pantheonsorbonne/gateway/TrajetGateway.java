package fr.pantheonsorbonne.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.dto.TrajetCompletDTO;


import fr.pantheonsorbonne.exception.TrajetCommunicationException;
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

            // Le type de rep est une liste

            List<?> rawList = (List<?>) response;
            return rawList.stream()
                    .map(item -> objectMapper.convertValue(item, TrajetCompletDTO.class)) // Convertir chaque élément
                    .toList();


        } catch (Exception e) {
             throw new TrajetCommunicationException("Erreur lors de la récupération des trajets depuis le service distant", e);

        }
    }
}

