package org.sena.restClient;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.sena.domain.ResponseApiDB;

@Path("/characters")
@RegisterRestClient(configKey = "api-dragon-ball")
public interface ImagesProxy {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    ResponseApiDB getWarriorsImages(@QueryParam("limit") int limit);
}
