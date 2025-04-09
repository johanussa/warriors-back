package org.sena.domain;

import lombok.Data;

import java.util.List;

@Data
public class Player {

    private int idPlayer;
    private String name;
    private String nickname;
    private double score;
    private int lives;
    private List<Warrior> warriors;
}
