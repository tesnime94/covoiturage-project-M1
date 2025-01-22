package fr.pantheonsorbonne.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.pantheonsorbonne.dto.TrajetAvecSousTrajetsDTO;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;


@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:fetchIntermediateCities")
                .routeId("fetch-intermediate-cities-route")
                .log("Requête envoyée à Overpass : ${header.data}")
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader("Content-Type", constant("application/x-www-form-urlencoded"))
                .setBody(simple("data=${header.data}"))
                .to("https://overpass-api.de/api/interpreter")
                .log("Response from Overpass API: ${body}")
                .onException(Exception.class)
                .log("Erreur lors de l'appel à Overpass : ${exception.message}")
                .handled(true);

        from("direct:nominatimReverse")
                .routeId("nominatim-reverse-route")
                .setHeader("CamelHttpMethod", constant("GET"))
                .toD("https://nominatim.openstreetmap.org/reverse?format=json&lat=${header.latitude}&lon=${header.longitude}")
                .log("Response from Nominatim: ${body}");

        from("direct:nominatimSearch")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.HTTP_QUERY, simple("q=${header.ville}&format=json"))
                .to("https://nominatim.openstreetmap.org/search")
                .log("Requête envoyée à Nominatim : ${header.CamelHttpQuery}")
                .log("Réponse brute Nominatim : ${body}");

        from("direct:validateUser")
                .log("Envoi de l'email utilisateur au service User pour validation")
                .marshal().json()
                .to("sjms2:M1.UserService") // Envoi du message à UserService
                .unmarshal().json()
                .log("réponse reçue: ${body}") // Attente de la réponse
                .choice()
                .when(body().isEqualTo(false))
                .log("Utilisateur introuvable")
                .throwException(new RuntimeException("L'utilisateur n'existe pas dans la base de données"))
                .otherwise()
                .log("Utilisateur valide")
                .end();

        // Configurer l'ObjectMapper pour la route à envoyer à recherche
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // Configurer le JacksonDataFormat
        JacksonDataFormat jsonDataFormat = new JacksonDataFormat(mapper, TrajetAvecSousTrajetsDTO.class);

        from("sjms2:M1.TrajetService")
                .log("Envoie de trajet pour ${body}")
                .bean("customTrajetService", "getTrajetsWithSousTrajetsByVilleDepart(${body})") // Appelle le service
                .log("Résultat du trajet : ${body}")
                .marshal(jsonDataFormat)
        ;
    }
}


