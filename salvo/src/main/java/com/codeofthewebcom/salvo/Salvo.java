package com.codeofthewebcom.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.ArrayList;

import java.util.List;


@Entity
public class Salvo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ElementCollection
    @Column(name = "locationSalvo")
    private List<String> locationSalvo = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    // atrbts
    private int turn;

    // constr.
    public Salvo() {
    }

    public Salvo(int turn, List<String> locationSalvo) {
        this.turn = turn;
        this.locationSalvo = locationSalvo;

    }
    public Salvo(int turn, List<String> locationSalvo, GamePlayer gamePlayer) {
        this.turn = turn;
        this.locationSalvo = locationSalvo;
        this.gamePlayer=gamePlayer;
    }

    // getters
    public long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public List<String> getLocationSalvo() {
        return locationSalvo;
    }

    public int getTurn() {
        return turn;
    }

    public void setGamePlayer(GamePlayer gamePlayers) {
        this.gamePlayer = gamePlayers;
    }
}
