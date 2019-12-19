
// OBJECT VUE
var app = new Vue({
    el: '#app',
    data: {
        listajugadores:[],
        playersList: [],
        games: [],
        gamePlayers: [],
        playerName: "",
        player: {},
        returnGame: false,
        hrefGame: ""
    },
    methods: {
        vlogin: function (evt) {
            login(evt)
        },
        vlogout: function (evt) {
            logout(evt)
        },
        vregister: function (evt) {
            register(evt)
        },
        vcheckPlayer: function (data) {
            checkPlayer(data);
        },
        vreturnGame: function (data) {
            return returnGame(data);
        },
        vredirection: function (data) {
            return redirection(data);
        },
        vcreateGame: function (evt) {
            gameCreate(evt)
        },
        vjoinGame:function(nn){
            joinGame(nn);
        }
    },
    filters: {
        moment: function (data) {
            if (!data) {
                return ""
            } else {
                return moment(data).format('l') + " " + moment(data).format('LT');
            }
        }
    }
})

//------------------------------------------------------------------------------------------------------------------------

// LOAD JSON

var myJson;
// GET PARA OBTENER EL JSON
$(function () {
    function loadData() {
        $.get("/api/games")
            .done(function (data) {
                loadJson(data);
            })
            .fail(function (jqXHR, textStatus) {
                console.log("Failed: " + textStatus);
            });
    };
    loadData();
});

//------------------------------------------------------------------------------------------------------------------------

// TRANSFIERE EL JSON AL VUE OBJECT

function loadJson(data) {
    myJson = data;
    app.games = myJson.games;
    //app.player = data.player;
    leaderboard(myJson);

    checkPlayer(data);

}

function checkPlayer(data) {
    if (data.player == null) {
        app.playerName = "Guest";
        app.player = "Guest";

    } else {
        app.player = data.player;
        app.playerName = data.player.email;
    }
}

//------------------------------------------------------------------------------------------------------------------------

//LISTA DE GAMEPLAYERS
function leaderboard(data) {
    var games = data.games;
    for (i = 0; i < games.length; i++) {
        for (j = 0; j < games[i].gamePlayers.length; j++) {
            var wins = 0;
            var lost = 0;
            var ties = 0;
            var total = 0;
            var jugador = {};
            var gamePlayer = games[i].gamePlayers[j];
            index = app.listajugadores.findIndex(gp => buscarJugador(gp, gamePlayer));
            if (app.listajugadores.length > 0 && index >= 0) {
                if (gamePlayer.score != null) {
                    app.listajugadores[index].total += gamePlayer.score.score;
                    switch (gamePlayer.score.score) {
                        case 1:
                            app.listajugadores[index].wins++;
                            break;
                        case 0:
                            app.listajugadores[index].lost++;
                            break;
                        case 0.5:
                            app.listajugadores[index].ties++;
                    }
                }
            } else {
                if (gamePlayer.score != null) {
                    switch (gamePlayer.score.score) {
                        case 1:
                            wins++;
                            break;
                        case 0:
                            lost++;
                            break;
                        case 0.5:
                            ties++;
                    }

                    total += gamePlayer.score.score;
                    jugador = {
                        "email": gamePlayer.player.email,
                        "total": total,
                        "wins": wins,
                        "lost": lost,
                        "ties": ties
                    };
                    app.listajugadores.push(jugador);
                }
            }
        }
    }
    app.playersList = app.listajugadores;
}

function buscarJugador(gp, gamplayer) {
    return gp.email === gamplayer.player.email;
}

function login(evt) {
    evt.preventDefault();
    var form = evt.target.form;
    $.post("/api/login",
        {
            username: form["username"].value,
            password: form["password"].value
        }, function () {
            window.location.reload();
        },
    )
        .fail(function (error) {
            alert("ERROR - REGISTRATION ERROR ");
            console.log(error);
        });
}

function logout(evt) {
    evt.preventDefault();
    $.post("/api/logout", function () {
        window.location.reload();
    })
        .fail(registerFail);
}

function register(evt) {
    evt.preventDefault();
    var form = evt.target.form;
    $.post("/api/players",
        {
            username: form["username"].value,
            password: form["password"].value
        }, function () {
            console.log("registrado");
            login(evt);

        })
        .fail(registerFail);
}


function returnGame(game) {
    var result = false;
    if (app.player !== null) {
        for (j = 0; j < game.gamePlayers.length; j++) {
            if (game.gamePlayers[j].player.id === app.player.id) {
                result = true;
            }
        }
    }

    return result;
}

function redirection(game) {
    for (j = 0; j < game.gamePlayers.length; j++) {
        if (game.gamePlayers[j].player.id === app.player.id) {
            return "http://localhost:8080/web/game.html?gp=" + game.gamePlayers[j].id;
        }
    }
}

function gameCreate() {
    $.post("/api/games"
        ,function(data){
            return window.location="http://localhost:8080/web/game.html?gp="+ data.gpId;
        }
    )
        .fail(registerFail);
}

function joinGame(nn) {
    $.post("/api/game/"+nn+"/players"
        ,function(data){
            return window.location="http://localhost:8080/web/game.html?gp="+ data.Success.id;
        }
    )
        .fail(registerFail(error));
}

function registerFail(error) {
    return alert("ERROR " + error.responseJSON.error);
}

