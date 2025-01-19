package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.dto.ResaDTO;
import fr.pantheonsorbonne.service.ResaService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/resa")
public class ResaResource {

    @Inject
    private ResaService resaService;

    @GET
    @Path("/{resaNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResaById(@PathParam("id") Long id) {
        try {
            ResaDTO resaDTO = resaService.getResaById(id);
            return Response.ok(resaDTO).build(); // Retourne un 200 OK avec le résultat JSON
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Reservation not found with id: " + id)
                    .build(); // Retourne un 404 si la réservation n'existe pas
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Transactional
    public Response createResa(CreateResaRequest request) {
        try {
            TrajetPrincipal trajetPrincipal = trajetService.createTrajet(
                    request.getVilleDepart(),
                    request.getVilleArrivee(),
                    request.getDate(),
                    request.getHoraire(),
                    request.getNombreDePlaces(),
                    request.getPrix(),
                    request.getConducteurMail()
            );
            return Response.ok(trajetPrincipal).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la création du trajet : " + e.getMessage()).build();
        }
    }
}
