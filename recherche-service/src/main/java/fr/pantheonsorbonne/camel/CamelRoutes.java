package fr.pantheonsorbonne.camel;

import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:getTrajets")
            //.routeId("GetTrajetsRoute")
            .log("Demande de trajets pour la ville : ${body}")

            // Appel au service Trajet via JMS
            .marshal().json() // Convertit l'entrée en JSON si nécessaire
            .to("sjms2:M1.TrajetService")
            .unmarshal().json() // Traite la réponse JSON en tant qu'objet Camel

            // Logique de validation ou transformation si nécessaire
            .choice()
                .when(body().isNull())
                    .log("Aucun trajet trouvé pour la ville : ${body}")
                    .throwException(new RuntimeException("Aucun trajet trouvé"))
                .otherwise()
                    .log("Trajets récupérés avec succès : ${body}")
            .end();
    }
}

