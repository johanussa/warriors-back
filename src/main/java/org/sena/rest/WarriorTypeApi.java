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
import org.sena.domain.WarriorType;
import org.sena.helper.utils.ValidationGroups;
import org.sena.service.WarriorTypeService;

import java.util.List;

@Tag(name = "Warrior-Type")
@Path("/internal/warrior-type")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WarriorTypeApi {

    private static final Logger LOG = Logger.getLogger(WarriorTypeApi.class);

    @Inject
    WarriorTypeService warriorTypeService;

    @GET
    @Path("/{warriorTypeId}")
    @Operation(
            summary = "Consultar un tipo de guerrero",
            description = "Permite consultar un tipo de guerrero por su identificador"
    )
    public Response getWarriorTypeById(
            @Parameter(
                    name = "warriorTypeId",
                    description = "Identificador del tipo de guerrero a consultar",
                    required = true,
                    example = "0e1bd9ed-25b0-43e2-b55a-1013c98e63e5"
            )
            @PathParam("warriorTypeId") String warriorTypeId
    ) {

        LOG.infof("@getWarriorTypeById API > Inicia consulta del tipo de guerrero con ID: %s", warriorTypeId);

        WarriorType warriorType = warriorTypeService.getWarriorTypeById(warriorTypeId);

        LOG.infof("getWarriorTypeById API > Finaliza consulta. Se obtuvo el siguiente tipo: %s", warriorType);

        return Response.ok().entity(warriorType).build();
    }

    @GET
    @Path("/allWarriorTypes")
    @Operation(
            summary = "Obtener listado de tipos de guerrero",
            description = "Permite obtener el listado de los tipos de guerreros registrados en base de datos"
    )
    public Response getAllTypesWarrior() {

        LOG.info("@getAllTypesWarrior API > Inicia servicio consulta de listado de tipos de guerreros en base de datos");

        List<WarriorType> warriorTypes = warriorTypeService.getListTypesWarrior();

        LOG.infof("@getAllTypesWarrior API > Finaliza servicio de consulta de listado. Se obtuvo el registro " +
                "de: %s tipo(s) de guerrero", warriorTypes.size());

        return Response.ok().entity(warriorTypes).build();
    }

    @POST
    @Path("/create")
    @Operation(
            summary = "Crear un tipo de guerrero",
            description = "Permite realizar el registro de un nuevo tipo de guerrero en base de datos"
    )
    public Response createWarriorType(
            @RequestBody(
                    name = "warriorType",
                    description = "Información del tipo de guerrero que se va a registrar"
            )
            @NotNull(message = "Debe ingresar la información del tipo de guerrero que se registrará")
            @Valid @ConvertGroup(to = ValidationGroups.Post.class) WarriorType warriorType
    ) {

        LOG.infof("@createWarriorType API > Inicia API para almacenar tipo de guerrero con data: %s", warriorType);

        warriorTypeService.createWarriorType(warriorType);

        LOG.infof("@createWarriorType API > Finaliza API. El tipo guerrero: %s se registro correctamente", warriorType);

        return Response.ok().status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/update")
    @Operation(
            summary = "Actualizar un tipo de guerrero",
            description = "Permite actualizar un tipo de guerrero por su identificador con la información proporcionada"
    )
    public Response updateWarriorTpe(
            @RequestBody(
                    name = "warriorType",
                    description = "Información del tipo de guerrero que se actualizará"
            )
            @NotNull(message = "Debe ingresar la información del tipo de guerrero a actualizar")
            @Valid @ConvertGroup(to = ValidationGroups.Put.class) WarriorType warriorType
    ) {

        LOG.infof("@updateWarriorTpe API > Inicia ejecucion de API para actualizar el tipo de guerrero con " +
                "la data: %s", warriorType);

        warriorTypeService.updateWarriorType(warriorType);

        LOG.infof("@updateWarriorTpe API > Finaliza API para actualizar el tipo de guerrero con ID: %s. El " +
                "tipo de guerrero fue actualizado correctamente", warriorType.getIdWarriorType());

        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/delete/{warriorTypeId}")
    @Operation(
            summary = "Eliminar un tipo de guerrero",
            description = "Permite eliminar el registro de un tipo de guerrero por el identificador proporcionado"
    )
    public Response deleteWarriorType(
            @Parameter(
                    name = "warriorTypeId",
                    description = "Identificador del tipo de guerrero a eliminar",
                    required = true,
                    example = "0e1bd9ed-25b0-43e2-b55a-1013c98e63e5"
            )
            @NotBlank(message = "Debe ingresar el valor del identificador del tipo de guerrero a eliminar")
            @PathParam("warriorTypeId") String warriorTypeId
    ) {

        LOG.infof("@deleteWarriorType API > Inicia API para eliminar tipo guerrero con ID: %s", warriorTypeId);

        warriorTypeService.deleteWarriorType(warriorTypeId);

        LOG.infof("@deleteWarriorType API > Finaliza API para eliminar el registro del tipo de guerrero con el " +
                "identificador: %s. El registro fue eliminado correctamente", warriorTypeId);

        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }
}
