package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.dto.CreateNotificationRequest;
import fr.pantheonsorbonne.dto.NotificationDTO;
import fr.pantheonsorbonne.entity.NotificationType;
import fr.pantheonsorbonne.service.NotificationService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    @Inject
    private NotificationService notificationService;

    /**
     * Endpoint pour créer une nouvelle notification.
     *
     * @param request Les détails de la notification à créer.
     * @return La notification créée.
     */
    @POST
    @Transactional
    public Response createNotification(@Valid CreateNotificationRequest request) {
        NotificationType type = request.getType();
        String userId = request.getUserId();
        String message = request.getMessage();

        var notification = notificationService.createNotification(userId, message, type);
        return Response.status(Response.Status.CREATED).entity(notification).build();
    }

    /**
     * Endpoint pour récupérer les notifications d'un utilisateur.
     *
     * @param userId L'ID de l'utilisateur dont les notifications doivent être récupérées.
     * @return Liste des notifications.
     */
    @GET
    @Path("/{userId}")
    public Response getNotificationsByUserId(@PathParam("userId") String userId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return Response.ok(notifications).build();
    }
}
