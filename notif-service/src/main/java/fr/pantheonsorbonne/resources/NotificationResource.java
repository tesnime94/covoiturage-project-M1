package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.entity.Notification;
import fr.pantheonsorbonne.entity.NotificationType;
import fr.pantheonsorbonne.service.NotificationService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    private final NotificationService notificationService;

    public NotificationResource(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @POST
    public Response createNotification(Notification notification) {
        notificationService.createNotification(notification.userId, notification.message, notification.type);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{userId}")
    public List<Notification> getNotifications(@PathParam("userId") String userId) {
        return notificationService.getNotificationsForUser(userId);
    }
}
