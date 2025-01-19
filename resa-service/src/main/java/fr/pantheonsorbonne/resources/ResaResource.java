package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.dto.CreateResaRequest;
import fr.pantheonsorbonne.dto.ResaDTO;
import fr.pantheonsorbonne.entity.Resa;
import fr.pantheonsorbonne.exception.PaymentException;
import fr.pantheonsorbonne.exception.ResaNotFoundException;
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
        } catch (ResaNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createResa(CreateResaRequest request) {
        try {
            Resa resa = resaService.createResa(
                    request.getTrajetNumber(),
                    request.getAmount(),
                    request.getCardHolderName(),
                    request.getCardNumber(),
                    request.getExpirationDate(),
                    request.getCvc()
            );
            return Response.ok(resa).build();
        } catch (PaymentException e) {
            throw new RuntimeException(e);
        }
    }
}
