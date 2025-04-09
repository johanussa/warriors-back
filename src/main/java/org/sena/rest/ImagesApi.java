package org.sena.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;
import org.sena.service.ImagesService;

import java.util.List;

@Path("/internal")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImagesApi {

    private static final Logger LOG = Logger.getLogger(ImagesApi.class);

    @Inject
    ImagesService imagesService;

    @GET
    @Path("/images")
    @Operation(
            summary = "Obtener imagenes",
            description = "Permite obtener las imagenes de los guerreros disponibles"
    )
    public Response getImages() {

        LOG.info("@getImages API > Inicia obtencion de las imagenes de los guerreros disponibles");

        List<String> images = imagesService.getWarriorsImages();

        LOG.infof("@getImages API > Finaliza obtencion de imagenes, Se obtuvo: %s imagenes", images.size());

        return Response.ok().entity(images).build();
    }
}
