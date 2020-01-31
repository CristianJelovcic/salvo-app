package com.codeofthewebcom.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public Score getScore() {
        return this.player.getScore(this.game);
    }

    public Optional<GamePlayer> getOpponentGamePlayer() {
        return this.game.getGamePlayers().stream().filter(gp -> gp.getId() != this.id).findFirst();
    }

    public Enum<State> getGameState(){
        Enum<State> gameState = State.UNDEFINED;
        Optional<GamePlayer> opponentGamePlayer = this.getGame().getGamePlayers().stream().filter(gp -> gp.getId() != this.getId()).findFirst();
        if (!opponentGamePlayer.isPresent()) {
            gameState = State.WAIT_OPPONENT_JOIN;
        } else{
            if (this.getShips().isEmpty())
                gameState = State.PLACE_SHIPS;
            else if (opponentGamePlayer.get().getShips().isEmpty())
                gameState = State.WAIT_OPPONENT_SHIPS;
            else{
                int myTurn = this.getSalvo().stream().mapToInt(Salvo::getTurn).max().orElse(0);
                int opponentTurn = opponentGamePlayer.get().getSalvo().stream().mapToInt(Salvo::getTurn).max().orElse(0);
                if (this.getId() < opponentGamePlayer.get().getId() && myTurn == opponentTurn)
                    gameState = State.ENTER_SALVOES;
                else if (this.getId() < opponentGamePlayer.get().getId() && myTurn > opponentTurn)
                    gameState =  State.WAIT_OPPONENT_SALVOES;
                else if (this.getId() > opponentGamePlayer.get().getId() && myTurn < opponentTurn)
                    gameState = State.ENTER_SALVOES;
                else if (this.getId() > opponentGamePlayer.get().getId() && myTurn == opponentTurn)
                    gameState =  State.WAIT_OPPONENT_SALVOES;
                List<Map<String, Object>> mySinks = this.getSinks(myTurn, opponentGamePlayer.get().getShips(), this.getSalvo());
                List<Map<String, Object>> opponentSinks = this.getSinks(opponentTurn, this.getShips(), opponentGamePlayer.get().getSalvo());
                if (myTurn == opponentTurn && mySinks.size() == 5 && mySinks.size() > opponentSinks.size())
                    gameState = State.WIN;
                else if (myTurn == opponentTurn && opponentSinks.size() == 5 && opponentSinks.size() > mySinks.size())
                    gameState = State.LOSE;
                else if (myTurn == opponentTurn && mySinks.size() == 5 && opponentSinks.size() == 5)
                    gameState = State.TIE;
            }
        }

        return gameState;
    }
    private List<Map<String, Object>> getSinks (int turn, Set<Ship> ships, Set<Salvo> salvos){
        List<String> allShots = new ArrayList<>();
        salvos.stream()
                .filter(salvo -> salvo.getTurn() <= turn)
                .forEach(salvo -> allShots.addAll(salvo.getLocationSalvo()));

        return ships.stream()
                .filter(trf -> allShots.containsAll(trf.getLocationShip()))
                .map(ship -> queryShipDTO(ship))
                .collect(Collectors.toList());
    }
    private Map<String, Object> queryShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getTypeShip());
        return dto;

    }
}