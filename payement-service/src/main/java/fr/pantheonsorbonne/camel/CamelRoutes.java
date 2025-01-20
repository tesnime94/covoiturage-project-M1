package fr.pantheonsorbonne.camel;

import fr.pantheonsorbonne.dto.ReservationDTO;
import org.apache.camel.builder.RouteBuilder;

public class CamelRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("sjms2:M1.PayementService")
                .log("Réception des données de paiement : ${body}")
                .unmarshal().json(ReservationDTO.class)
                .bean("payment", "processPayment")
                .log("Résultat de la paiement : ${body}")
                .marshal().json();

              

    }
}
