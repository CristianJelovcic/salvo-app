package com.codeofthewebcom.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.*;

import java.util.stream.Collectors;


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

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void addShots (List<String> locationSalvo) {

        this.locationSalvo.addAll(locationSalvo);
    }



    public List<String> getHits(){
        if(this.getGamePlayer().getOpponentGamePlayer().isPresent() && this.getGamePlayer().getShips().size()!=0) {
            return locationSalvo.stream()
                    .filter(shot -> this.getGamePlayer().getOpponentGamePlayer().get().getShips().stream().anyMatch(ship -> ship.getLocationShip().contains(shot)))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> getSinks (){
        List<String> allShots = new ArrayList<>();
        this.gamePlayer.getSalvo().stream()
                .filter(salvo -> salvo.getTurn() <= this.turn)
                .forEach(salvo -> allShots.addAll(salvo.getLocationSalvo()));

        return this.gamePlayer.getShips().stream()
                .filter(ship -> allShots.containsAll(ship.getLocationShip()))
                .map(ship -> queryShipDTO(ship))
                .collect(Collectors.toList());
    }

    private Map<String, Object> queryShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getTypeShip());
        return dto;

    }

}
