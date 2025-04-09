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
import org.sena.domain.Power;
import org.sena.helper.utils.ValidationGroups;
import org.sena.service.PowerService;

import java.util.List;

@Tag(name = "Power")
@Path("/internal/power")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PowerApi {

    private static final Logger LOG = Logger.getLogger(PowerApi.class);

    @Inject
    PowerService powerService;

    @GET
    @Path("/allPowers")
    @Operation(
            summary = "Obtener listado de poderes",
            description = "Permite obtener el listado de los poderes registrados en base de datos"
    )
    public Response getAllPowers() {

        LOG.info("@getAllPowers API > Inicia servicio de consulta de listado de los poderes en base de datos");

        List<Power> powerList = powerService.getPowersList();

        LOG.infof("@getAllPowers API > Finaliza servicio de consulta de listado. Se obtuvo el registro " +
                "de: %s poderes", powerList.size());

        return Response.ok().entity(powerList).build();
    }

    @GET
    @Path("/{powerId}")
    @Operation(
            summary = "Consultar un poder",
            description = "Permite consultar un poder por su identificador"
    )
    public Response getPowerById(
            @Parameter(
                    name = "powerId",
                    description = "Identificador del poder a consultar",
                    required = true,
                    example = "0e1bd9ed-25b0-43e2-b55a-1013c98e63e5"
            )
            @PathParam("powerId") String powerId
    ) {

        LOG.infof("@getPowerById API > Inicia API de consulta del poder con identificador: %s", powerId);

        Power power = powerService.getPowerById(powerId);

        LOG.infof("getPowerById API > Finaliza consulta. Se obtuvo el siguiente poder: %s", power);

        return Response.ok().entity(power).build();
    }

    @POST
    @Path("/create")
    @Operation(
            summary = "Crear un poder",
            description = "Permite realizar el registro de un nuevo poder en base de datos"
    )
    public Response createPower(
            @RequestBody(
                    name = "power",
                    description = "Información del poder que se va a registrar"
            )
            @NotNull(message = "Debe ingresar la información del poder que se registrará")
            @Valid @ConvertGroup(to = ValidationGroups.Post.class) Power power
    ) {

        LOG.infof("@createPower API > Inicia servicio API para almacenar un poder con la data: %s", power);

        powerService.createPower(power);

        LOG.infof("@createPower API > Finaliza API. El poder: %s se registro correctamente", power);

        return Response.ok().status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/update")
    @Operation(
            summary = "Actualizar un poder",
            description = "Permite actualizar un poder por su identificador con la información proporcionada"
    )
    public Response updatePower(
            @RequestBody(
                    name = "power",
                    description = "Información del poder que se actualizará"
            )
            @NotNull(message = "Debe ingresar la información del poder a actualizar")
            @Valid @ConvertGroup(to = ValidationGroups.Put.class) Power power
    ) {

        LOG.infof("@updatePower API > Inicia ejecucion de API para actualizar el poder con la data: %s", power);

        powerService.updatePower(power);

        LOG.infof("@updatePower API > Finaliza ejecucion de API para actualizar el poder con identificador" +
                ": %s. El poder fue actualizado correctamente", power.getIdPower());

        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/delete/{powerId}")
    @Operation(
            summary = "Eliminar un poder",
            description = "Permite eliminar el registro de un poder por el identificador proporcionado"
    )
    public Response deletePower(
            @Parameter(
                    name = "powerId",
                    description = "Identificador del poder a eliminar",
                    required = true,
                    example = "0e1bd9ed-25b0-43e2-b55a-1013c98e63e5"
            )
            @NotBlank(message = "Debe ingresar el valor del identificador del poder a eliminar")
            @PathParam("powerId") String powerId
    ) {

        LOG.infof("@deletePower API > Inicia API para eliminar el poder con ID: %s", powerId);

        powerService.deletePowerById(powerId);

        LOG.infof("@deletePower API > Finaliza API para eliminar el registro del poder con el ID: %s. El " +
                "registro fue eliminado correctamente", powerId);

        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }
}
