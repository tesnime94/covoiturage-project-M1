package fr.pantheonsorbonne.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.LoggingLevel;

public class NotificationRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        // Route pour consommer les messages Kafka sur le topic notifications
        from("kafka:notifications?brokers=localhost:9092")
                .routeId("KafkaNotificationConsumer")
                .log(LoggingLevel.INFO, "Message reçu depuis Kafka: ${body}")
                .to("direct:processNotification");

        // Traitement des notifications
        from("direct:processNotification")
                .routeId("ProcessNotification")
                .log("Traitement de la notification : ${body}")
                .bean("notificationProcessor", "process");

        // Route pour publier une notification sur Kafka
        from("direct:sendNotificationToKafka")
                .routeId("SendNotificationToKafka")
                .log("Envoi d'une notification à Kafka : ${body}")
                .to("kafka:notifications?brokers=localhost:9092");

        // (Optionnel) Route pour envoyer une notification à Artemis JMS
        from("direct:sendNotificationToArtemis")
                .routeId("SendNotificationToArtemis")
                .log("Envoi d'une notification à Artemis : ${body}")
                .to("sjms2:queue:notifications");
    }
}
