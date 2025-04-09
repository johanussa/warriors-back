package org.sena.domain;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

@Data
public class ResponseApiDB implements Serializable {

    @Schema(examples = "['https://dragonball-api.com/characters/goku_normal.webp']")
    private List<Warrior> items;
}
