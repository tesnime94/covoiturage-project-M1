package fr.pantheonsorbonne.camel;

import org.apache.camel.builder.RouteBuilder;

import fr.pantheonsorbonne.exception.TrajetNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:getTrajets")
              
                .log("Demande de trajets pour la ville : ${body}")

                .to("sjms2:M1.TrajetService") 

                .unmarshal().json() // Traite la réponse JSON en tant qu'objet Camel

                // Logique de validation ou transformation si nécessaire
                .choice()
                .when(body().isNull())
                .log("Aucun trajet trouvé pour la ville : ${body}")
                .throwException(new TrajetNotFoundException("Aucun trajet trouvé pour la ville : " + body()))
                .otherwise()
                .log("Trajets récupérés avec succès : ${body}")
                .end();

                from("direct:sendResultatReservation")
                .log("Envoi des résultats au microservice réservation : ${body}")
                .marshal().json()
                .to("sjms2:M1.ResaService")
                .log("Résultat envoyé avec succès au microservice réservation.");
    }
}

