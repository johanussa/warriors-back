package org.sena.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
import org.sena.domain.Warrior;
import org.sena.helper.utils.ValidationGroups;
import org.sena.service.WarriorService;

import java.util.List;

@Tag(name = "Warrior")
@Path("/internal/warrior")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WarriorApi {

    private static final Logger LOG = Logger.getLogger(WarriorApi.class);

    @Inject
    WarriorService warriorService;

    @GET
    @Path("/{warriorId}")
    @Operation(
            summary = "Obtener un guerrero",
            description = "Permite obtener el registro de un guerrero por su identificador"
    )
    public Response getWarriorById(
            @Parameter(
                    name = "warriorId",
                    example = "2d4f1313-4c22-4576-8c97-a9b3416fe416",
                    required = true,
                    description = "Identificador del guerrero a consultar"
            )
            @PathParam("warriorId") String warriorId
    ) {

        LOG.infof("@getWarriorById API > Inicia API para obtener registro de guerrero con ID: %s", warriorId);

        Warrior warrior = warriorService.getWarriorById(warriorId);

        LOG.infof("@getWarriorById API > Finaliza ejecucion de API para obtener registro de guerrero en base " +
                "de datos. Guerrero obtenido: %s", warrior);

        return Response.ok().entity(warrior).build();
    }

    @GET
    @Path("/allWarriors")
    @Operation(
            summary = "Obtener todos los guerreros",
            description = "Permite obtener el listado de todos los guerreros registrados"
    )
    public Response getAllWarriors() {

        LOG.info("@getAllWarriors API > Inicia API para obtener el listado de todos los guerreros registrados");

        List<Warrior> warriors = warriorService.getAllWarriors();

        LOG.infof("@getAllWarriors API > Finaliza ejecucion de API para obtener el listado de todos los " +
                "guerreros registrados. Se obtuvo: %s registros en base de datos", warriors.size());

        return Response.ok().entity(warriors).build();
    }

    @POST
    @Path("/create")
    @Operation(
            summary = "Crear un guerrero",
            description = "Permite crear el registro de un nuevo guerrero con los datos proporcionados"
    )
    public Response createWarrior(
            @RequestBody(
                    name = "warrior",
                    description = "Información del guerrero a registrar"
            )
            @NotNull(message = "Debe enviar la información del guerrero que se registrará")
            @Valid @ConvertGroup(to = ValidationGroups.Post.class) Warrior warrior
    ) {

        LOG.infof("@createWarrior API > Inicia ejecucion de API para registrar un guerrero con la data: %s", warrior);

        warriorService.createWarriorRegistry(warrior);

        LOG.infof("@createWarrior API > Finaliza ejecucion de API para registrar un guerrero con la data: %s. " +
                "El guerrero se registro exitosamente", warrior);

        return Response.status(Response.Status.CREATED).entity(warrior).build();
    }

    @PUT
    @Path("/update")
    @Operation(
            summary = "Actualizar un guerrero",
            description = "Permite actualizar un guerrero con la información proporcionada"
    )
    public Response updateWarrior(
            @RequestBody(
                    name = "warrior",
                    description = "Información del guerrero que se actualizará"
            )
            @NotNull(message = "Debe enviar la información del guerrero a actualizar")
            @Valid @ConvertGroup(to = ValidationGroups.Put.class) Warrior warrior
    ) {

        LOG.infof("@updateWarrior API > Inicia ejecucion de API para actualizar el registro del guerrero " +
                "con la informacion: %s", warrior);

        Warrior warriorUpdated = warriorService.updateWarriorRegistry(warrior);

        LOG.infof("@updateWarrior API > Finaliza ejecucion de API para actualizar el registro de un guerrero. " +
                "La informacion actualizada fue: %s", warrior);

        return Response.ok().entity(warriorUpdated).build();
    }

    @DELETE
    @Path("/delete/{warriorId}")
    @Operation(
            summary = "Eliminar un guerrero",
            description = "Permite eliminar un guerrero por su identificador"
    )
    public Response deleteWarrior(
            @Parameter(
                    name = "warriorId",
                    description = "Identificador del guerrero a eliminar",
                    example = "12345",
                    required = true
            )
            @PathParam("warriorId") String warriorId
    ) {

        LOG.infof("@deleteWarrior API > Inicia ejecicion de API para eliminar el registro del guerrero con " +
                "identificador: %s en base de datos", warriorId);

        warriorService.deleteWarriorRegistry(warriorId);

        LOG.infof("@deleteWarrior API > Finaliza ejecicion de API para eliminar registro de un guerrero. El " +
                "guerrero con identificador: %s fue eliminado correctamente", warriorId);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
