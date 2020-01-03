package com.codeofthewebcom.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.Set;
import java.util.HashSet;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scores = new HashSet<>();

    //atributos
    private String userName;

    private String password;

    //constructores
    public Player() {
    }

    public Player(String user, String pass) {
        userName = user;
        password= pass;
    }

    //metodos
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }


    // getters & setters
    public String getUserName() {
        return userName;
    }

    public long getID() {
        return id;
    }

    public Score getScore(Game game) {
        return scores.stream().filter( score -> score.getGame().getId() == game.getId()).findFirst().orElse(null);
    }

    public String getPassword() {
        return password;
    }

/*    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }*/
}


