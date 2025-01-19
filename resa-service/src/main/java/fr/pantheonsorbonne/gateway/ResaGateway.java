package fr.pantheonsorbonne.gateway;

import jakarta.inject.Inject;
import org.apache.camel.ProducerTemplate;

public class ResaGateway {

    @Inject
    private ProducerTemplate producerTemplate;

    public boolean processPayment(String paymentResponse){
        Object response = producerTemplate.requestBody("direct:processPayment", paymentResponse);
        return Boolean.parseBoolean(response.toString());
    }
}
