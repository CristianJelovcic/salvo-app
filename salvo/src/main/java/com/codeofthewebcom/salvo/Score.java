package com.codeofthewebcom.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


@Entity
public class Score {
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

    //ATBTS
    private LocalDateTime finishDate;
    private float score;

    // CONSTR
    public Score() {

    }

    public Score(Game game, Player player, float score) {
        this.game = game;
        this.player = player;
        this.score = score;
        this.finishDate = LocalDateTime.now();
    }

    public Score(Player player, Game game, float score, LocalDateTime finishDate) {
        this.game = game;
        this.player = player;
        this.score = score;
        this.finishDate = finishDate;
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

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public float getScore() {
        return score;
    }
}
