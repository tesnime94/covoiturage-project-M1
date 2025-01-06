package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.entity.SousTrajet;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/sous-trajets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SousTrajetResource {

    @Inject
    private SousTrajetDAO sousTrajetDAO;
    
    @GET
    @Path("/{trajetPrincipalId}")
    public List<SousTrajet> getSousTrajetsByTrajetPrincipal(@PathParam("trajetPrincipalId") Long trajetPrincipalId) {
        return sousTrajetDAO.findByTrajetPrincipalId(trajetPrincipalId);
    }


}
