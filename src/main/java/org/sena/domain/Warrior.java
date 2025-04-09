package org.sena.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.sena.helper.utils.ConvertToJson;
import org.sena.helper.utils.ValidationGroups;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Warrior implements Serializable {

    @BsonId
    @Schema(examples = "5742401d-77c9-4940-a3b3-1ae38e5556d3")
    @Null(message = "El valor del campo idWarrior debe ser nulo", groups = ValidationGroups.Post.class)
    @NotBlank(message = "El valor del campo idWarrior no puede ser nulo o vacío", groups = ValidationGroups.Put.class)
    private String idWarrior;

    @Schema(examples = "Vegeta")
    @NotBlank(message = "El campo name no puede ser nulo o vacío")
    @Size(min = 3, message = "El campo name debe contener al menos 3 caracteres")
    private String name;

    @NotBlank(message = "El campo image no puede ser nulo o vacío")
    @Schema(examples = "https://dragonball-api.com/characters/vegeta_normal.webp")
    private String image;

    @Schema(examples = "100.0")
    @NotNull(message = "El campo health no puede ser nulo o vacío")
    @Positive(message = "El valor del campo health debe ser mayor a cero (0)")
    @Max(value = 1000, message = "El valor de la salud debe ser máximo de 1000")
    private Double health;

    @Schema(examples = "100.0")
    @NotNull(message = "El campo energy no puede ser nulo o vacío")
    @Positive(message = "El valor del campo energy debe ser mayor a cero (0)")
    @Max(value = 1000, message = "El valor de la energía debe ser máximo de 1000")
    private Double energy;

    @Schema(examples = "0e1bd9ed-25b0-43e2-b55a-1013c98e63e5")
    @NotNull(message = "El campo breedId (id raza) no puede ser nulo o vacío")
    @Size(min = 36, max = 36, message = "El valor del campo breedId debe contener 36 caracteres")
    private String breedId;

    @Schema(examples = "407659bb-4fe2-463a-8401-3c48e79cf141")
    @NotNull(message = "El campo warriorTypeId no puede ser nulo o vacío")
    @Size(min = 36, max = 36, message = "El valor del campo warriorTypeId debe contener 36 caracteres")
    private String warriorTypeId;

    @NotNull(message = "La lista de poderes no puede ser nula o estar vacía")
    @Size(min = 5, message = "El guerrero debe tener al menos cinco (5) referencias de poderes")
    @Schema(examples = "[\"2d4f1313-4c22-4576-8c97-a9b3416fe416\", \"e37b7e13-9843-4747-9ef0-ad9ef99ac4a2\"]")
    private List<String> powersId;

    private Breed breed;
    private WarriorType warriorType;
    private List<Power> powers;

    @Override
    public String toString() { return ConvertToJson.toJson(this); }
}
