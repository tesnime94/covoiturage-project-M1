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
            Resa resa = resaService.getResaById(id);
            ResaDTO dto = new ResaDTO(
                    resa.getResaNumber(),
                    resa.getTrajetNumber(),
                    resa.getAmount(),
                    resa.getCardHolderName(),
                    resa.getCardNumber(),
                    resa.getExpirationDate(),
                    resa.getCvc()
            );
            return Response.ok(dto).build();
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
