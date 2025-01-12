package fr.pantheonsorbonne.gateway;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.ProducerTemplate;

@ApplicationScoped
public class UserGateway {

    @Inject
    private ProducerTemplate producerTemplate;

    public Boolean validateUserByMail(String userMail) {
        Object response = producerTemplate.requestBody("direct:validateUser", userMail);
        return Boolean.parseBoolean(response.toString());
    }

}
