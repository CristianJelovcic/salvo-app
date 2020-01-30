package com.codeofthewebcom.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @OneToMany(mappedBy = "gamePlayer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Salvo> salvos = new HashSet<>();

    //ATBTS
    private LocalDateTime joinDate;

    //CONSTRUCTORES
    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
        joinDate = LocalDateTime.now();
    }


    // GETTERS & SETTERS
    public long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }



    public void addShip(Ship ship) {
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    public void addSalvo(Salvo salvo) {
        salvo.setGamePlayer(this);
        salvos.add(salvo);
    }


    public Set<Ship> getShips() {
        return ships;
    }

    public Set<Salvo> getSalvo() {
        return salvos;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public Score getScore(){
        return this.player.getScore(this.game);
    }

    public Optional<GamePlayer> getOpponentGamePlayer(){
        return this.game.getGamePlayers().stream().filter(gp -> gp.getId() != this.id).findFirst();
    }



}
