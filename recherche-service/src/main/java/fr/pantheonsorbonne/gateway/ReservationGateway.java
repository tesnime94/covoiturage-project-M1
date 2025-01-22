package fr.pantheonsorbonne.gateway;

import org.apache.camel.ProducerTemplate;

import fr.pantheonsorbonne.dto.ResultatDTO;
import fr.pantheonsorbonne.exception.ReservationCommunicationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped

public class ReservationGateway {

    @Inject
    private ProducerTemplate producerTemplate;

    // Envoie du resultat à la reservation

    public void envoyerResultatReservation(ResultatDTO resultatDTO) {
        try {
            producerTemplate.sendBody("direct:sendResultatReservation", resultatDTO);
        } catch (Exception e) {
            throw new ReservationCommunicationException("Erreur lors de l'envoi des résultats au microservice réservation.", e);
        }
    }
}

