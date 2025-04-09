package org.sena.domain;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.sena.helper.utils.ConvertToJson;
import org.sena.helper.utils.ValidationGroups;

import java.io.Serializable;

@Data
public class Power implements Serializable {

    @BsonId
    @Schema(examples = "4752401d-77c9-2485-a3b3-1ae38e5559e7")
    @Null(message = "El valor del campo idPower debe ser nulo", groups = ValidationGroups.Post.class)
    @NotBlank(message = "El valor del campo idPower no puede ser nulo o vacío", groups = ValidationGroups.Put.class)
    private String idPower;

    @Schema(examples = "Kamehameha")
    @NotBlank(message = "El campo name no puede ser nulo o vacío")
    @Size(min = 5, message = "El campo name debe contener al menos 5 caracteres")
    private String name;

    @Schema(examples = "40.5")
    @NotNull(message = "EL campo damage no puede ser nulo o vacío")
    @Positive(message = "El valor del campo damage debe ser mayor a cero (0)")
    private Double damage;

    @Schema(examples = "Onda de energía que aumenta con carga")
    @NotBlank(message = "El campo effect no puede ser ulo o vacío")
    @Size(min = 5, message = "El campo effect debe contener al menos 5 caracteres")
    private String effect;

    @NotBlank(message = "El campo description no puede ser nulo o vacío")
    @Size(min = 12, message = "El campo description debe contener al menos 12 caracteres")
    @Schema(examples = "Técnica de la Escuela Tortuga que lanza una potente ráfaga de energía desde las manos")
    private String description;

    @Schema(examples = "30.0")
    @NotNull(message = "EL campo powerEnergyConsumed no puede ser nulo o vacío")
    @Positive(message = "El valor del campo powerEnergyConsumed debe ser mayor a cero (0)")
    private Double powerEnergyConsumed;

    @Override
    public String toString() { return ConvertToJson.toJson(this); }
}
