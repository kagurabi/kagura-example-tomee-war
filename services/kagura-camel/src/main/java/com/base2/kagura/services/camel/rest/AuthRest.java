package com.base2.kagura.services.camel.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("/")
public class AuthRest {
    /**
     * Allows you to login and see reports
     * @param user username
     * @param password Password, plaintext for the mean time
     * @return Auth token in JSON tag "token", or a plain text error in JSON tag "error".
     */
    @Path("login/{user}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getAuthToken(@PathParam("user") String user, String password){return null;}
}
