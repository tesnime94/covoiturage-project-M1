package fr.pantheonsorbonne.camel;

import org.apache.camel.builder.RouteBuilder;

public class CamelRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("sjms2:M1.UserService")
                .log("Validation utilisateur pour ${body}")
                .unmarshal().json(String.class)
                .bean("userValidator", "validateUser")
                .log("Résultat de la validation : ${body}")
                .marshal().json()
                ;
    }
}
