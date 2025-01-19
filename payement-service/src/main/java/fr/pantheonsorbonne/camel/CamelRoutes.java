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
                .choice()
                .when(simple("${body} == true"))
                .log("Paiement validé")
                .otherwise()
                .log("Paiement refusé")
                .end();

    }
}
