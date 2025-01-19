package fr.pantheonsorbonne.camel;

import org.apache.camel.builder.RouteBuilder;

public class CamelRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("direct:processPayment")
                .log("Envoi des détails du paiement au service Payment pour traitement")
                .marshal().json() // Conversion en JSON avant l'envoi
                .to("sjms2:M1.PaymentService") // Envoi du message au service Payment via un broker
                .unmarshal().json() // Conversion de la réponse en objet utilisable
                .log("Réponse reçue : ${body}") // Log de la réponse reçue
                .choice()
                .when(body().isEqualTo(false))
                .log("Paiement échoué")
                .throwException(new RuntimeException("Le paiement a été refusé par le service Payment"))
                .otherwise()
                .log("Paiement validé avec succès")
                .end();

    }
}
