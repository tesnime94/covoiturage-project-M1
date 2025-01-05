package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.entity.SousTrajet;
import fr.pantheonsorbonne.exception.ResourceNotFoundException;
import fr.pantheonsorbonne.service.SousTrajetService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/sous-trajets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SousTrajetResource {

    @Inject
    private SousTrajetDAO sousTrajetDAO;
    @Inject
    private SousTrajetService sousTrajetService;

    @GET
    @Path("/{trajetPrincipalId}")
    public List<SousTrajet> getSousTrajetsByTrajetPrincipal(@PathParam("trajetPrincipalId") Long trajetPrincipalId) {
        return sousTrajetDAO.findByTrajetPrincipalId(trajetPrincipalId);
    }

    @GET
    @Path("/{id}")
    public Response getSousTrajetById(@PathParam("id") Long id) {
        try {
            SousTrajet sousTrajet = sousTrajetService.getSousTrajetById(id);
            return Response.ok(sousTrajet).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
