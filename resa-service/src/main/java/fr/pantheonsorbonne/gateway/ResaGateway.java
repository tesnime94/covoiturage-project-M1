package fr.pantheonsorbonne.gateway;

import jakarta.inject.Inject;
import org.apache.camel.ProducerTemplate;

public class ResaGateway {

    @Inject
    private ProducerTemplate producerTemplate;

    public boolean processPayment(){
        Object response = producerTemplate.requestBody("direct:processPayment");
        return Boolean.parseBoolean(response.toString());
    }
}
