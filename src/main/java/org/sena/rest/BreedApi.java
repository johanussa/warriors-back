package org.sena.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.sena.domain.Breed;
import org.sena.helper.utils.ValidationGroups;
import org.sena.service.BreedService;

import java.util.List;

@Tag(name = "Breed")
@Path("/internal/breed")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BreedApi {

    private static final Logger LOG = Logger.getLogger(BreedApi.class);

    @Inject
    BreedService breedService;

    @GET
    @Path("/{idBreed}")
    @Operation(
            summary = "Consultar una raza",
            description = "Permite consultar una raza por su identificador"
    )
    public Response getBreedById(
            @Parameter(
                    name = "idBreed",
                    description = "Identificador de la raza a consultar",
                    required = true,
                    example = "0e1bd9ed-25b0-43e2-b55a-1013c98e63e5"
            )
            @NotBlank(message = "Debe ingresar el valor del identificador de la raza")
            @PathParam("idBreed") String idBreed
    ) {

        LOG.infof("@getBreedById API > Inicia ejecucion de API para obtener el registro de la raza con " +
                "identificador: %s", idBreed);

        Breed breed = breedService.getBreedById(idBreed);

        LOG.infof("@getBreedById API > Finaliza ejecucion de API para obtener la raza por el identificador " +
                "con identificador: %s. La raza obtenida es: %s", idBreed, breed);

        return Response.ok().entity(breed).build();
    }

    @GET
    @Path("/allBreeds")
    @Operation(
            summary = "Obtener listado de razas",
            description = "Permite obtener el listado de las razas registradas en base de datos"
    )
    public Response getBreedList() {

        LOG.info("@getBreedList API > Inicia ejecucion de API para obtener listado de las razas de guerreros registradas");

        List<Breed> breeds = breedService.getBreedList();

        LOG.infof("@getBreedList API > Finaliza ejecucion de API para obtener el listado de las razas en " +
                "base de datos. Se obtuvo: %s registros", breeds.size());

        return Response.ok().entity(breeds).build();
    }

    @POST
    @Path("/create")
    @Operation(
            summary = "Crear una raza",
            description = "Permite realizar el registro de una nueva raza"
    )
    public Response createBreed(
            @RequestBody(
                    name = "breed",
                    description = "Información de la raza que se va a registrar"
            )
            @NotNull(message = "Debe ingresar la información de la raza que se registrará")
            @Valid @ConvertGroup(to = ValidationGroups.Post.class) Breed breed
    ) {

        LOG.infof("@createBreed API > Inicia ejecucion de API para almacenar la raza con la data: %s", breed);

        breedService.createBreed(breed);

        LOG.infof("@createBreed API > Finaliza ejecucion de API. La raza: %s se registro correctamente", breed);

        return Response.ok().status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/update")
    @Operation(
            summary = "Actualizar una raza",
            description = "Permite actualizar una raza por su identificador con la información proporcionada"
    )
    public Response updateBreed(
            @RequestBody(
                    name = "breed",
                    description = "Información de la raza que se actualizará"
            )
            @NotNull(message = "Debe ingresar la información de la raza a actualizar")
            @Valid @ConvertGroup(to = ValidationGroups.Put.class) Breed breed
    ) {

        LOG.infof("@updateBreed API > Inicia ejecucion de API para actualizar la raza con identificador: %s " +
                "con la data: %s", breed.getIdBreed(), breed);

        breedService.updateBreed(breed);

        LOG.infof("@updateBreed API > Finaliza ejecucion de API para actualizar la raza con identificador: " +
                "%s. La raza fue actualizada correctamente", breed.getIdBreed());

        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/delete/{idBreed}")
    @Operation(
            summary = "Eliminar una raza",
            description = "Permite eliminar el registro de una raza por el identificador proporcionado"
    )
    public Response deleteBreed(
            @Parameter(
                    name = "idBreed",
                    description = "Identificador de la raza a eliminar",
                    required = true,
                    example = "0e1bd9ed-25b0-43e2-b55a-1013c98e63e5"
            )
            @NotBlank(message = "Debe ingresar el valor del identificador de la raza a eliminar")
            @PathParam("idBreed") String idBreed
    ) {

        LOG.infof("@deleteBreed API > Inicia ejecucion de API para eliminar la raza con identificador: %s", idBreed);

        breedService.deleteBreed(idBreed);

        LOG.infof("@deleteBreed API > Finaliza ejecucion de API para eliminar el registro de la raza con el " +
                "identificador: %s. El registro fue eliminado correctamente", idBreed);

        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }
}
