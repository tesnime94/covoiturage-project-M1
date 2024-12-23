package fr.pantheonsorbonne.resources;

import fr.pantheonsorbonne.dto.UserDTO;
import fr.pantheonsorbonne.exception.InvalidUserException;
import fr.pantheonsorbonne.exception.UserAlreadyExistWithTheSameEmail;
import fr.pantheonsorbonne.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("user")
public class UserResource {
    @Inject
    UserService userService;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id){
        UserDTO user = userService.getUserByID(id);
        if(user == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(user).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUser(UserDTO userDTO){
        try {
            Long userId = userService.checkAndSaveUser(userDTO);
            return Response.created(URI.create("/user/" + userId)).build();
        } catch (InvalidUserException e) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());


        } catch (UserAlreadyExistWithTheSameEmail e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT).build());
        }
    }
}
