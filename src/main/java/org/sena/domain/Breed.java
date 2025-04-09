package org.sena.domain;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.sena.helper.utils.ConvertToJson;
import org.sena.helper.utils.ValidationGroups;

import java.io.Serializable;

@Data
public class Breed implements Serializable {

    @BsonId
    @Schema(examples = "2405741d-4940-77c9-a3b3-1ae36d3558e5")
    @Null(message = "El valor del campo idBreed debe ser nulo", groups = ValidationGroups.Post.class)
    @NotBlank(message = "El valor del campo idBreed no puede ser nulo o vacío", groups = ValidationGroups.Put.class)
    private String idBreed;

    @Schema(examples = "Saiyajin")
    @NotBlank(message = "El campo name no puede ser nulo o vacío")
    @Size(min = 3, message = "El campo name debe contener al menos 3 caracteres")
    private String name;

    @NotBlank(message = "El campo description no puede ser nulo o vacío")
    @Size(min = 12, message = "El campo description debe contener al menos 12 caracteres")
    @Schema(examples = "Guerreros con gran potencial de combate. Se vuelven más fuertes después de cada batalla")
    private String description;

    @Schema(examples = "1.6")
    @NotNull(message = "EL campo resistance no puede ser nulo o vacío")
    @Positive(message = "El valor del campo resistance debe ser mayor a cero (0)")
    private Double resistance;

    @Override
    public String toString() { return ConvertToJson.toJson(this); }
}
