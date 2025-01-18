package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.dao.HistoriqueRechercheDAO;
import fr.pantheonsorbonne.dao.RequeteTestDAO;
import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dao.TrajetDAO;
import fr.pantheonsorbonne.dto.RequeteDTO;
import fr.pantheonsorbonne.dto.ResultatDTO;
import fr.pantheonsorbonne.service.RechercheService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/recherche")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RechercheRessources {

    @Inject
    private RechercheService rechercheService;

    @Inject
    private TrajetDAO trajetDAO;

    @Inject
    private SousTrajetDAO sousTrajetDAO;

    @Inject
    private HistoriqueRechercheDAO histoDAO;

    @Inject
    private RequeteTestDAO testDAO;





    @POST
    @Path("/RechercheTrajet")
    public Response rechercherTrajets(RequeteDTO requeteDTO) {
        try {
            // Appel du service pour effectuer la recherche
            ResultatDTO resultat = rechercheService.rechercherTrajets(requeteDTO);

            // Retourne une réponse avec le statut approprié
            if (resultat.success()) {
                return Response.ok(resultat).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(resultat).build();
            }
        } catch (Exception e) {
            // Gestion des erreurs inattendues
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Une erreur est survenue lors de la recherche : " + e.getMessage())
                    .build();
        }
    }

   










}