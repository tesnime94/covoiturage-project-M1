package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.dto.CreateTrajetRequest;
import fr.pantheonsorbonne.dto.TrajetPrincipalDTO;
import fr.pantheonsorbonne.entity.TrajetPrincipal;
import fr.pantheonsorbonne.exception.TrajetNotFoundException;
import fr.pantheonsorbonne.service.TrajetService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/trajets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TrajetResource {

    @Inject
    private TrajetService trajetService;

    @POST
    @Transactional
    public Response createTrajet(CreateTrajetRequest request) {
        try {
            TrajetPrincipal trajetPrincipal = trajetService.createTrajet(
                    request.getVilleDepart(),
                    request.getVilleArrivee(),
                    request.getHoraire(),
                    request.getPrix(),
                    request.getConducteurId()
            );
            return Response.ok(trajetPrincipal).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getTrajetById(@PathParam("id") Long id) {
        try {
            TrajetPrincipal trajet = trajetService.getTrajetById(id);
            TrajetPrincipalDTO dto = new TrajetPrincipalDTO(
                    trajet.getId(),
                    trajet.getVilleDepart(),
                    trajet.getVilleArrivee(),
                    trajet.getHoraire(),
                    trajet.getPrix(),
                    trajet.getConducteurId()
            );
            return Response.ok(dto).build();
        } catch (TrajetNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response getAllTrajets() {
        List<TrajetPrincipal> trajets = trajetService.getAllTrajets();
        return Response.ok(trajets).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteTrajetById(@PathParam("id") Long id) {
        boolean deleted = trajetService.deleteTrajetById(id);
        if (deleted) {
            return Response.ok("Trajet supprimé avec succès.").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Trajet non trouvé.").build();
    }
}
