package com.server.nanny.boundaries;


import com.server.nanny.exceptions.UserAlreadyExistsException;
import com.server.nanny.exceptions.UserNotFoundException;
import com.server.nanny.models.User;
import com.server.nanny.services.UserServiceImpl;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResources {

    @Inject
    private UserServiceImpl userService ;


    /**
     *
     * @param user
     * @return Response entity
     * @throws  UserAlreadyExistsException
     * @apiNote : used to  create admin account
     */

    @POST
    public Response createUser(@Valid User user){
         try {
             return Response.ok(userService.createUser(user)).build() ;
         } catch (UserAlreadyExistsException  e){
             return  Response.status(400, e.getMessage()).build();
         }


    }

    /**
     *
     * @param user
     * @return status
     * @apiNote  this methode is used by the admin to add users
     */

    @POST()
    @Path("/add")
    @RolesAllowed("ADMIN")
    public  Response addUser( @Valid User user){
        try {
            var createdUser = userService.addUser(user);
            return Response.ok(createdUser.getForname() + createdUser.getSurname() + "is added successfully ").build();
        } catch(UserAlreadyExistsException e) {
            return Response.status(400 , e.getMessage()).build() ;

        }

    }


    /**
     *
     * @param email
     * @return status
     * @apiNote  this  methode is used by the Admin to delete users
     */
    @DELETE()
    @Path("/{email}")
    @RolesAllowed("ADMIN")
    public  Response deleteUser(@PathParam("email") String email){
        try {
              userService.delete(email);
              return  Response.ok().build() ;
        }catch(UserNotFoundException e){
            return  Response.status(400 , e.getMessage()).build() ;
        }

    }











}
