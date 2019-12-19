package com.codeofthewebcom.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;


    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ShipRepository shipRepository;


    @Autowired
    PasswordEncoder passwordEncoder;

 //POST Request to place all five Current GamePlayer's Ships
    @PostMapping(path = "/games/players/{gamePlayerId}/ships")
    public  ResponseEntity<Map<String, Object>> addShips(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Set<Ship> shipsList){
        GamePlayer currentGamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
        if(isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "You need to Log In, please Enter your email and password"), HttpStatus.UNAUTHORIZED);
        }
        if(currentGamePlayer == null){
            return new ResponseEntity<>(makeMap("error", "No GamePlayer found for you"), HttpStatus.UNAUTHORIZED);
        }
        if(!(currentGamePlayer.getPlayer().equals(getLoggedUser(authentication)))){
            return new ResponseEntity<>(makeMap("error", "You can only manage your own ships"), HttpStatus.UNAUTHORIZED);
        }
        if(!(currentGamePlayer.getShips().size() == 0)){
            return new ResponseEntity<>(makeMap("error", "Ships already in place"), HttpStatus.FORBIDDEN);
        }
        if(!(shipsList.size() == 5)){
            return new ResponseEntity<>(makeMap("error", "You must place All five Warships on your Grid before start firing!"), HttpStatus.FORBIDDEN);
        }
        for (Ship ship : shipsList) {
            currentGamePlayer.addShip(ship);
            shipRepository.save(ship);
        }
        return new ResponseEntity<>(makeMap("ships", "your warships were placed!"), HttpStatus.CREATED);

    }



    private Boolean isLegalShips(Set<Ship> shipsList){
        Boolean result = true;
        for (Ship ship : shipsList) {
            if (!checkLength(ship) || !checkAlignement(ship)) {
                result = false;
            }
        }
        return result;
    }

    private Boolean checkLength(Ship ship){
        Boolean result = false;
        switch(ship.getTypeShip()){
            case "Carrier":
                if(ship.getLocationShip().size() == 5){
                    result = true;
                }
                break;
            case "Battleship":
                if(ship.getLocationShip().size() == 4){
                    result = true;
                }
                break;
            case "Destroyer":
                if(ship.getLocationShip().size() == 3){
                    result = true;
                }
                break;
            case "Submarine":
                if(ship.getLocationShip().size() == 3){
                    result = true;
                }
                break;
            case "Patrol boat":
                if(ship.getLocationShip().size() == 2){
                    result = true;
                }
                break;
            default:
                result = false;
                break;
        }
        return result;
    }
    private Boolean checkAlignement(Ship ship){
        //Boolean result = true;
        //return result;
        return true;
    }




    // CREA LOS DATOS DE MUESTRA PARA TODOS LOS JUEGOS
    @GetMapping("/games")
    public Map<String, Object> getAll(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if (isGuest(authentication)) {
            dto.put("player", null);
        } else {
            Player player = playerRepository.findByUserName(authentication.getName());
            dto.put("player", queryPlayerDTO(player));

        }
        dto.put("games", gameRepository.findAll().stream().map(game -> queryGameDTO(game)).collect(Collectors.toList()));
        return dto;
    }

    @PostMapping(path = "/games")
    public ResponseEntity<Object> gameCreate(Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
        } else {
            Player player = playerRepository.findByUserName(authentication.getName());
            Game newGame = gameRepository.save(new Game());
            GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(newGame, player));
            return new ResponseEntity<>(makeMap("gpId", newGamePlayer.getId()), HttpStatus.CREATED);
        }
    }

    @PostMapping(path = "/game/{id}/players")
    public ResponseEntity<Object> joinGame(@PathVariable Long id, Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
        }
        Player playerX = playerRepository.findByUserName(authentication.getName());
        Optional<Game> gameX= gameRepository.findById(id);
        if (!gameX.isPresent()){
            return new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.FORBIDDEN);
        }
        if(gameX.get().getGamePlayers().size() > 1){
            return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);
        }
        GamePlayer gamePlayerX = gamePlayerRepository.save(new GamePlayer(gameX.get(), playerX));
        return new ResponseEntity<>(makeMap("Success", queryGamePlayerDTO(gamePlayerX)), HttpStatus.CREATED);
    }

    // CREA LOS DATOS DE MUESTRA PARA UN JUGADOR EN UN JUEGO
    @GetMapping("/game_view/{id}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable Long id, Authentication authentication) {
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(id);
        String optionalPlayer = optionalGamePlayer.get().getPlayer().getUserName();
        String thisPlayer = authentication.getName();

        if (optionalPlayer.compareTo(thisPlayer)!=0) {
            return new ResponseEntity<>(makeMap("error", "Access error"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(queryGameViewDTO(optionalGamePlayer.get()), HttpStatus.OK);
    }

    @PostMapping(path = "/players")
    public ResponseEntity<Object> register(
            @RequestParam String username, @RequestParam String password) {

        Player player;

        if (username.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findOneByUserName(username) != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.FORBIDDEN);
        }

        player = playerRepository.save(new Player(username, passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("Player", player.getUserName()), HttpStatus.CREATED);
    }

    // ESTE METODO DTO CREA UN JSON PARA VISTA DEL JUGADOR
    private Map<String, Object> queryGameViewDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("created", gamePlayer.getJoinDate());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(game_Player -> queryGamePlayerDTO(game_Player)).collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips().stream().map(ship -> queryShipDTO(ship)));
        dto.put("salvos", gamePlayer.getGame().getGamePlayers().stream().flatMap(gameP -> gameP.getSalvo().stream().map(salvo -> querySalvoDTO(salvo))).collect(Collectors.toList()));

        return dto;
    }

    // ESTE METODO DTO CREA UN JSON PARA VISTA DE UNA INSTANCIA DE JUEGO
    private Map<String, Object> queryGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(game_Player -> queryGamePlayerDTO(game_Player)).collect(Collectors.toList()));
        return dto;
    }

    // ESTE METODO DTO CREA UN JSON PARA VISTA DE UNA INSTANCIA DEL JUGADOR EN UN JUEGO
    private Map<String, Object> queryGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", queryPlayerDTO(gamePlayer.getPlayer()));
        dto.put("score", queryScoreDTO(gamePlayer.getScore()));

        return dto;

    }

    // ESTE METODO DTO CREA UN JSON PARA VISTA DE UN JUGADOR
    private Map<String, Object> queryPlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getID());
        dto.put("email", player.getUserName());
        return dto;

    }

    // ESTE METODO DTO CREA UN JSON PARA VISTA DE LAS NAVES DE UN JUEGO
    private Map<String, Object> queryShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getTypeShip());
        dto.put("location", ship.getLocationShip());
        return dto;

    }

    // ESTE METODO DTO CREA UN JSON PARA VISTA DE LAS SALVOS DE UN JUEGO
    private Map<String, Object> querySalvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player", salvo.getGamePlayer().getPlayer().getID());
        dto.put("turn", salvo.getTurn());
        dto.put("salvoLocation", salvo.getLocationSalvo());
        return dto;

    }

    // ESTE METODO DTO CREA UN JSON PARA VISTA DE LOS SCORE DE UN GAMEPLAYER
    private Map<String, Object> queryScoreDTO(Score score) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if (score != null) {
            dto.put("id", score.getId());
            dto.put("score", score.getScore());
            dto.put("finishDate", score.getFinishDate());
            return dto;
        }
        return null;
    }


    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Player getLoggedUser(Authentication authentication) {
        return playerRepository.findByUserName(authentication.getName());
    }

    private static final String[] EMPTY_ARRAY = new String[0];

}
