package com.codeofthewebcom.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.ArrayList;

import java.util.List;


@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ElementCollection
    @Column(name = "locationShip")
    private List<String> locationShip = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    // atrbts
    private String typeShip;

    // constr.
    public Ship() {
    }

    ;

    public Ship(String typeShip, List<String> locationShip) {
        this.typeShip = typeShip;
        this.locationShip = locationShip;
        //Carrier
        //Battleship
        //Submarine
        //Destroyer
        //Patrol Boat
    }

    // getters
    public long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public List<String> getLocationShip() {
        return locationShip;
    }

    public String getTypeShip() {
        return typeShip;
    }

    public void setGamePlayer(GamePlayer gamePlayers) {
        this.gamePlayer = gamePlayers;
    }
}
