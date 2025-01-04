package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dto.CreateTrajetRequest;
import fr.pantheonsorbonne.entity.SousTrajet;
import fr.pantheonsorbonne.entity.TrajetPrincipal;
import fr.pantheonsorbonne.service.TrajetService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("/trajets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TrajetResource {

    @Inject
    private TrajetService trajetService;

    @Inject
    private SousTrajetDAO sousTrajetDAO;

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
        TrajetPrincipal trajetPrincipal = trajetService.getTrajetById(id);
        if (trajetPrincipal == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Trajet non trouvé").build();
        }
        return Response.ok(trajetPrincipal).build();
    }

    @GET
    @Path("/{trajetPrincipalId}")
    public List<SousTrajet> getSousTrajetsByTrajetPrincipal(@PathParam("trajetPrincipalId") Long trajetPrincipalId) {
        return sousTrajetDAO.findByTrajetPrincipalId(trajetPrincipalId);
    }

}
