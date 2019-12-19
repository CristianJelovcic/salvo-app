package com.codeofthewebcom.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gameplayerRepository, ScoreRepository scoreRepository) {
        return (args) -> {

            // INSTANCIA DE JUGADORES
            Player player1 = playerRepository.save(new Player("j.bauer@ctu.gov", passwordEncoder.encode("24")));
            Player player2 = playerRepository.save(new Player("c.obrian@ctu.gov", passwordEncoder.encode("42")));
            Player player3 = playerRepository.save(new Player("kim_bauer@gmail.com", passwordEncoder.encode("kb")));
            Player player4 = playerRepository.save(new Player("t.almeida@ctu.gov", passwordEncoder.encode("mole")));


            // INSTANCIA DE JUEGOS
            Game game1 = gameRepository.save(new Game());
            Game game2 = gameRepository.save(new Game(LocalDateTime.now().plusHours(1)));
            Game game3 = gameRepository.save(new Game(LocalDateTime.now().plusHours(2)));
            Game game4 = gameRepository.save(new Game(LocalDateTime.now().plusHours(3)));
            Game game5 = gameRepository.save(new Game(LocalDateTime.now().plusHours(4)));
            Game game6 = gameRepository.save(new Game(LocalDateTime.now().plusHours(5)));
            Game game7 = gameRepository.save(new Game(LocalDateTime.now().plusHours(6)));
            Game game8 = gameRepository.save(new Game(LocalDateTime.now().plusHours(7)));

            // INSTANCIA DE PARTIDAS DE JUEGOS
            GamePlayer gamePlayer1 = gameplayerRepository.save(new GamePlayer(game1, player1));
            GamePlayer gamePlayer2 = gameplayerRepository.save(new GamePlayer(game1, player2));
            GamePlayer gamePlayer3 = gameplayerRepository.save(new GamePlayer(game2, player1));
            GamePlayer gamePlayer4 = gameplayerRepository.save(new GamePlayer(game2, player2));
            GamePlayer gamePlayer5 = gameplayerRepository.save(new GamePlayer(game3, player2));
            GamePlayer gamePlayer6 = gameplayerRepository.save(new GamePlayer(game3, player4));
            GamePlayer gamePlayer7 = gameplayerRepository.save(new GamePlayer(game4, player2));
            GamePlayer gamePlayer8 = gameplayerRepository.save(new GamePlayer(game4, player1));
            GamePlayer gamePlayer9 = gameplayerRepository.save(new GamePlayer(game5, player4));
            GamePlayer gamePlayer10 = gameplayerRepository.save(new GamePlayer(game5, player2));
            GamePlayer gamePlayer11 = gameplayerRepository.save(new GamePlayer(game6, player3));
            GamePlayer gamePlayer12 = gameplayerRepository.save(new GamePlayer(game7, player4));
            GamePlayer gamePlayer13 = gameplayerRepository.save(new GamePlayer(game8, player3));
            GamePlayer gamePlayer14 = gameplayerRepository.save(new GamePlayer(game8, player4));


            // INSTANCIA 1 DE BARCOS PARA LA PARTIDA DE JUEGO  1
            Ship ship0 = new Ship("Destroyer", Arrays.asList("H2", "H3", "H4"));
            Ship ship1 = new Ship("Submarine", Arrays.asList("E1", "F1", "G1"));
            Ship ship2 = new Ship("Patrol Boat", Arrays.asList("B4", "B5"));
            gamePlayer1.addShip(ship0);
            gamePlayer1.addShip(ship1);
            gamePlayer1.addShip(ship2);

            Salvo salvo1 = new Salvo(1, Arrays.asList("B5", "C5", "F1"));
            Salvo salvo2 = new Salvo(2, Arrays.asList("F2", "D5"));
            gamePlayer1.addSalvo(salvo1);
            gamePlayer1.addSalvo(salvo2);
            gameplayerRepository.save(gamePlayer1);


            // INSTANCIA 2 DE BARCOS PARA LA PARTIDA DE JUEGO  1
            Ship ship3 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            Ship ship4 = new Ship("Patrol Boat", Arrays.asList("F1", "F2"));
            gamePlayer2.addShip(ship3);
            gamePlayer2.addShip(ship4);

            Salvo salvo3 = new Salvo(1, Arrays.asList("B4", "B5", "B6"));
            Salvo salvo4 = new Salvo(2, Arrays.asList("E1", "H3", "A2"));
            gamePlayer2.addSalvo(salvo3);
            gamePlayer2.addSalvo(salvo4);
            gameplayerRepository.save(gamePlayer2);

            // INSTANCIA 1 DE SCORE PARA LA PARTIDA DE JUEGO  1
            Score score1 = scoreRepository.save(new Score(game1, player1, 1, LocalDateTime.now().plusMinutes(30)));
            Score score2 = scoreRepository.save(new Score(game1, player2, 0, LocalDateTime.now().plusMinutes(30)));

//----------------------------------------------------------------------------------------------------------------------

            // INSTANCIA 1 DE BARCOS PARA LA PARTIDA DE JUEGO  2
            Ship ship5 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            Ship ship6 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gamePlayer3.addShip(ship5);
            gamePlayer3.addShip(ship6);

            Salvo salvo5 = new Salvo(1, Arrays.asList("A2", "A4", "G6"));
            Salvo salvo6 = new Salvo(2, Arrays.asList("A3", "H6"));
            gamePlayer3.addSalvo(salvo5);
            gamePlayer3.addSalvo(salvo6);
            gameplayerRepository.save(gamePlayer3);


            // INSTANCIA 2 DE BARCOS PARA LA PARTIDA DE JUEGO  2
            Ship ship7 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"));
            Ship ship8 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"));
            gamePlayer4.addShip(ship7);
            gamePlayer4.addShip(ship8);

            Salvo salvo7 = new Salvo(1, Arrays.asList("B5", "D5", "C7"));
            Salvo salvo8 = new Salvo(2, Arrays.asList("C5", "C6"));
            gamePlayer4.addSalvo(salvo7);
            gamePlayer4.addSalvo(salvo8);
            gameplayerRepository.save(gamePlayer4);

            Score score3 = scoreRepository.save(new Score(game2, player1, 0.5, LocalDateTime.now().plusMinutes(30)));
            Score score4 = scoreRepository.save(new Score(game2, player2, 0.5, LocalDateTime.now().plusMinutes(30)));

//----------------------------------------------------------------------------------------------------------------------

            // INSTANCIA 1 DE BARCOS PARA LA PARTIDA DE JUEGO  3
            Ship ship9 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            Ship ship10 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gamePlayer5.addShip(ship9);
            gamePlayer5.addShip(ship10);
            Salvo salvo9 = new Salvo(1, Arrays.asList("G6", "H6", "A4"));
            Salvo salvo10 = new Salvo(2, Arrays.asList("A2", "A3", "D8"));
            gamePlayer5.addSalvo(salvo9);
            gamePlayer5.addSalvo(salvo10);
            gameplayerRepository.save(gamePlayer5);


            // INSTANCIA 2 DE BARCOS PARA LA PARTIDA DE JUEGO  3
            Ship ship11 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"));
            Ship ship12 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"));
            gamePlayer6.addShip(ship11);
            gamePlayer6.addShip(ship12);
            Salvo salvo11 = new Salvo(1, Arrays.asList("H1", "H2", "H3"));
            Salvo salvo12 = new Salvo(2, Arrays.asList("E1", "F2", "G3"));
            gamePlayer6.addSalvo(salvo11);
            gamePlayer6.addSalvo(salvo12);
            gameplayerRepository.save(gamePlayer6);

            Score score5 = scoreRepository.save(new Score(game3, player2, 1, LocalDateTime.now().plusMinutes(30)));
            Score score6 = scoreRepository.save(new Score(game3, player4, 0, LocalDateTime.now().plusMinutes(30)));

            //----------------------------------------------------------------------------------------------------------------------

            // INSTANCIA 1 DE BARCOS PARA LA PARTIDA DE JUEGO  4
            Ship ship13 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            Ship ship14 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gamePlayer7.addShip(ship13);
            gamePlayer7.addShip(ship14);
            Salvo salvo13 = new Salvo(1, Arrays.asList("G6", "H6", "A4"));
            Salvo salvo14 = new Salvo(2, Arrays.asList("A2", "A3", "D8"));
            gamePlayer7.addSalvo(salvo13);
            gamePlayer7.addSalvo(salvo14);
            gameplayerRepository.save(gamePlayer7);


            // INSTANCIA 2 DE BARCOS PARA LA PARTIDA DE JUEGO  4
            Ship ship15 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"));
            Ship ship16 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"));
            gamePlayer8.addShip(ship15);
            gamePlayer8.addShip(ship16);
            Salvo salvo15 = new Salvo(1, Arrays.asList("H1", "H2", "H3"));
            Salvo salvo16 = new Salvo(2, Arrays.asList("E1", "F2", "G3"));
            gamePlayer8.addSalvo(salvo15);
            gamePlayer8.addSalvo(salvo16);
            gameplayerRepository.save(gamePlayer8);

            Score score7 = scoreRepository.save(new Score(game4, player2, 0.5, LocalDateTime.now().plusMinutes(30)));
            Score score8 = scoreRepository.save(new Score(game4, player1, 0.5, LocalDateTime.now().plusMinutes(30)));

            //----------------------------------------------------------------------------------------------------------------------

            // INSTANCIA 1 DE BARCOS PARA LA PARTIDA DE JUEGO  5
            Ship ship17 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            Ship ship18 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gamePlayer9.addShip(ship17);
            gamePlayer9.addShip(ship18);
            Salvo salvo17 = new Salvo(1, Arrays.asList("G6", "H6", "A4"));
            Salvo salvo18 = new Salvo(2, Arrays.asList("A2", "A3", "D8"));
            Salvo salvo200 = new Salvo(3, Arrays.asList("H1", "H8"));
            gamePlayer9.addSalvo(salvo17);
            gamePlayer9.addSalvo(salvo18);
            gamePlayer10.addSalvo(salvo200);
            gameplayerRepository.save(gamePlayer9);


            // INSTANCIA 2 DE BARCOS PARA LA PARTIDA DE JUEGO  5
            Ship ship19 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"));
            Ship ship20 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"));
            gamePlayer10.addShip(ship19);
            gamePlayer10.addShip(ship20);
            Salvo salvo19 = new Salvo(1, Arrays.asList("H1", "H2", "H3"));
            Salvo salvo20 = new Salvo(2, Arrays.asList("E1", "F2", "G3"));
            gamePlayer10.addSalvo(salvo19);
            gamePlayer10.addSalvo(salvo20);
            gameplayerRepository.save(gamePlayer10);

//----------------------------------------------------------------------------------------------------------------------

            // INSTANCIA 1 DE BARCOS PARA LA PARTIDA DE JUEGO  6
            Ship ship21 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            Ship ship22 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gamePlayer9.addShip(ship21);
            gamePlayer9.addShip(ship22);
            Salvo salvo21 = new Salvo(1, Arrays.asList("G6", "H6", "A4"));
            Salvo salvo22 = new Salvo(2, Arrays.asList("A2", "A3", "D8"));
            gamePlayer11.addSalvo(salvo21);
            gamePlayer11.addSalvo(salvo22);
            gameplayerRepository.save(gamePlayer11);

//----------------------------------------------------------------------------------------------------------------------

            // INSTANCIA 1 DE BARCOS PARA LA PARTIDA DE JUEGO  7
/*            Ship ship23 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            Ship ship24 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gamePlayer12.addShip(ship23);
            gamePlayer12.addShip(ship24);
            Salvo salvo23 = new Salvo(1, Arrays.asList("G6", "H6", "A4"));
            Salvo salvo24 = new Salvo(2, Arrays.asList("A2", "A3", "D8"));
            gamePlayer12.addSalvo(salvo23);
            gamePlayer12.addSalvo(salvo24);
            gameplayerRepository.save(gamePlayer12);*/

//----------------------------------------------------------------------------------------------------------------------

            // INSTANCIA 1 DE BARCOS PARA LA PARTIDA DE JUEGO  8
            Ship ship25 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            Ship ship26 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gamePlayer9.addShip(ship25);
            gamePlayer9.addShip(ship26);
            Salvo salvo25 = new Salvo(1, Arrays.asList("G6", "H6", "A4"));
            Salvo salvo26 = new Salvo(2, Arrays.asList("A2", "A3", "D8"));
            gamePlayer13.addSalvo(salvo25);
            gamePlayer13.addSalvo(salvo26);
            gameplayerRepository.save(gamePlayer13);

            // INSTANCIA 2 DE BARCOS PARA LA PARTIDA DE JUEGO  8
            Ship ship27 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"));
            Ship ship28 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"));
            gamePlayer14.addShip(ship27);
            gamePlayer14.addShip(ship28);
            Salvo salvo27 = new Salvo(1, Arrays.asList("G6", "H6", "A4"));
            Salvo salvo28 = new Salvo(2, Arrays.asList("A2", "A3", "D8"));
            gamePlayer14.addSalvo(salvo27);
            gamePlayer14.addSalvo(salvo28);
            gameplayerRepository.save(gamePlayer14);
        };
    }
}
