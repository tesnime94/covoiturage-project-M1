package fr.pantheonsorbonne.resources;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class NotificationConsumer {

    @Incoming("notifications") // Écoute le topic Kafka 'notifications'
    public void consumeNotification(String message) {
        System.out.println("Notification reçue : " + message);
        // Logique additionnelle pour traiter la notification
    }
}
