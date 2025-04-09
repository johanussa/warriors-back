package org.sena.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.sena.helper.utils.ConvertToJson;
import org.sena.helper.utils.ValidationGroups;

import java.io.Serializable;

@Data
public class WarriorType implements Serializable {

    @BsonId
    @Schema(examples = "4752401d-77c9-2485-a3b3-1ae38e5559e7")
    @Null(message = "El valor del campo idWarriorType debe ser nulo", groups = ValidationGroups.Post.class)
    @NotBlank(message = "El valor del campo idWarriorType no puede ser nulo o vacío", groups = ValidationGroups.Put.class)
    private String idWarriorType;

    @Schema(examples = "Luchador")
    @NotBlank(message = "El campo name no puede ser nulo o vacío")
    @Size(min = 3, message = "El campo name debe contener al menos 3 caracteres")
    private String name;

    @NotBlank(message = "El campo description no puede ser nulo o vacío")
    @Size(min = 12, message = "El campo description debe contener al menos 12 caracteres")
    @Schema(examples = "Guerrero con gran habilidad en el combate cuerpo a cuerpo y alto nivel de fuerza física")
    private String description;

    @Override
    public String toString() { return ConvertToJson.toJson(this); }
}
