package fr.pantheonsorbonne.resources;


import java.util.List;

import fr.pantheonsorbonne.dao.HistoriqueRechercheDAO;
import fr.pantheonsorbonne.dao.SousTrajetDAO;
import fr.pantheonsorbonne.dao.TrajetDAO;
import fr.pantheonsorbonne.entity.HistoriqueRecherche;
import fr.pantheonsorbonne.entity.Trajet;
import fr.pantheonsorbonne.service.RechercheService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;




@Path("/trajet")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class TrajetsRessource {

    @Inject
    private RechercheService rechercheService;

    @Inject
    private TrajetDAO trajetDAO;

    @Inject
    private SousTrajetDAO sousTrajetDAO;

    @Inject
    private HistoriqueRechercheDAO histoDAO;

    

@GET
@Path("/afficher-historique")
@Produces(MediaType.APPLICATION_JSON)
public Response afficherhistorique() {
    List<HistoriqueRecherche> histo = histoDAO.findAll();
    return Response.ok(histo).build();
}


    
@GET
@Path("/afficher-trajets")
@Produces(MediaType.APPLICATION_JSON)
public Response afficherTrajets() {
    List<Trajet> trajets = trajetDAO.findAll();
    return Response.ok(trajets).build();
}


@DELETE
@Path("/supprimer-tous-trajets")
@Transactional
public Response supprimerTousLesTrajets() {
    try {
        // Supprimer tous les sous-trajets
        sousTrajetDAO.deleteAll();

        // Supprimer tous les trajets
        trajetDAO.deleteAll();

        return Response.ok("Tous les trajets et sous-trajets ont été supprimés avec succès.").build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Erreur lors de la suppression des trajets : " + e.getMessage())
                .build();
    }
}

}
