package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.dto.CreateTrajetRequest;
import fr.pantheonsorbonne.entity.TrajetPrincipal;
import fr.pantheonsorbonne.service.TrajetService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;

@Path("/trajets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TrajetResource {

    @Inject
    private TrajetService trajetService;

    // Endpoint pour créer un trajet principal
    @POST
    public Response createTrajet(CreateTrajetRequest request) {
        try {
            TrajetPrincipal trajetPrincipal = trajetService.createTrajet(
                    request.getVilleDepart(),
                    request.getVilleArrivee(),
                    request.getHoraire(),
                    request.getPrix(),
                    request.getConducteurId()
            );
            return Response.status(Response.Status.CREATED).entity(trajetPrincipal).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la création du trajet : " + e.getMessage())
                    .build();
        }
    }

    // Endpoint pour récupérer un trajet principal par ID
    @GET
    @Path("/{id}")
    public Response getTrajetById(@PathParam("id") Long id) {
        TrajetPrincipal trajetPrincipal = trajetService.getTrajetById(id);
        if (trajetPrincipal == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Trajet non trouvé").build();
        }
        return Response.ok(trajetPrincipal).build();
    }
}
