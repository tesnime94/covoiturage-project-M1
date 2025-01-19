package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.dto.PaymentDTO;
import fr.pantheonsorbonne.service.PaymentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("payments")
public class PaymentResource {

    @Inject
    PaymentService paymentService;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentById(@PathParam("id") Long id) {
        PaymentDTO payment = paymentService.getPaymentById(id);
        if (payment == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(payment).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPayment(PaymentDTO paymentDTO) {
        Long paymentId = paymentService.savePayment(paymentDTO);
        return Response.created(URI.create("/payments/" + paymentId)).build();
    }
}
