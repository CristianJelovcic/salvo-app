const urlParams = new URLSearchParams(window.location.search);
const gpId = urlParams.get("gp");

//GET JSON
$(function () {
    function loadData() {
        $.get("/api/game_view/" + gpId)
            .done(function (data) {
                loadDATAJson(data);
            })
            .fail(function (jqXHR, textStatus) {
                console.log("Failed: " + textStatus);
            });
    };
    loadData();
});


// OBJECT VUE
var app = new Vue({
    el: '#app',
    data: {
        game_view: {},
        coordenadaX: ["", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
        coordenadaY: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        gamePlayer: [],
        viewer: "",
        viewerSalvos: [],
        oponenteSalvos: [],
        oponente: "",
        ships: [],
        viewerShips: [],
        shipLocation: [],
        opponentShips: [],
        location: [],
        salvoes: [],
        hits: [],
        allships: ["carrier", "battleship", "submarine", "destroyer", "patrol_boat"],
        placedShips: [
            {typeShip: "Carrier", locationShip: []},
            {typeShip: "Battleship", locationShip: []},
            {typeShip: "Destroyer", locationShip: []},
            {typeShip: "Submarine", locationShip: []},
            {typeShip: "Patrol Boat", locationShip: []}
        ]
    },
    methods: {
        vlogout: function (evt) {
            logout(evt)
        },
        postShips: function(){
            //creating the objecto to be sent as the Body of the Fetch
            this.placedShips[0].locationShip = ["A1","A2","A3","A4","A5"];
            this.placedShips[1].locationShip = ["B1","B2","B3","B4"];
            this.placedShips[2].locationShip = ["C1","C2","C3"];
            this.placedShips[3].locationShip = ["D1","D2","D3"];
            this.placedShips[4].locationShip = ["E1","E2"];

            if(this.placedShips[0].locationShip.length == 0 || this.placedShips[1].locationShip.length == 0 || this.placedShips[2].locationShip.length == 0 || this.placedShips[3].locationShip.length == 0 || this.placedShips[4].locationShip.length == 0){
                console.log("You must place All five Warships on your Grid before start firing!")
            }
            else{

                fetch('/api/games/players/' + gpId + '/ships', {
                    credentials: 'include',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    method: 'POST',
                    body: JSON.stringify(this.placedShips)
                })
                    .then(function(response){
                        if(response.status == 201)
                            location.reload();
                        else{
                            return response.json();
                        }
                    })
                    .then(function(json){
                        console.log(json);
                    })
                    .catch(function (er) {

                        console.log(er);
                    });
            }
        }

    }
})

// TRANSFIERE DE DATOS
function loadDATAJson(data) {
    var myJson = data;
    app.game_view = myJson;
    app.gamePlayer = myJson.gamePlayers;
    app.ships = myJson.ships;
    app.salvoes = myJson.salvos;
    selectShips(app.ships);
    listGamePlayers(app.gamePlayer);
    selectSalvos(app.salvoes);


}


function salvoHits(salvo) {
    salvo.salvoLocation.forEach(loc => {
        app.ships.forEach(ship => {
            ship.location.forEach(locShip => {
                if (locShip === loc) {
                    paintLocationSalvoHits(loc, salvo.turn);
                }
            })
        })
    });
}


// SELECCIONA EL SALVO
function selectLocationsSalvos(salvo) {
    if (salvo.player == app.viewerSalvos) {
        salvo.salvoLocation.forEach(loc => paintLocationSalvo(loc, salvo.turn));
    } else {
        salvoHits(salvo)
    }

}


// SELECCIONA EL SALVO
function selectSalvos(salvos) {
    salvos.forEach(loc => selectLocationsSalvos(loc));
}


// PINTA LA CELDA DE ACUERDO AL VALOR DE LA LOCACION LOCATION[I]
function paintLocationSalvo(location, turn) {
    var elemento = document.getElementById('s' + location);
    elemento.innerHTML = turn;
    elemento.classList.add("salvos");
}

// PINTA LAS SELDAS CON IMPACTO DE SALVOS EN SHIPS
function paintLocationSalvoHits(loc, turn) {
    var elemento = document.getElementById(loc);
    elemento.innerHTML = turn;
    elemento.classList.remove("ship");
    elemento.classList.add("salvosHits");
}


// SELECCIONA LA NAVE
function selectShips(ships) {
    ships.forEach(selectLocations);
}


// SELECCIONA LA LOCACION
function selectLocations(ship) {
    ship.location.forEach(loc => paintLocation(loc, ship.type));

}


// PINTA LA CELDA DE ACUERDO AL VALOR DE LA LOCACION LOCATION[I]
function paintLocation(locationShip) {
    var elemento = document.getElementById(locationShip);
    elemento.classList.add("ship");
}


// MOSTRAR LOS JUGADORES EN GAME VIEW
function listGamePlayers(gameplayers) {
    gameplayers.forEach(toShowPlayer)
}


// CONDICION PARA MOSTRAR LOS BARCOS DEL VIEWER
function toShowPlayer(gameplayer) {
    if (gameplayer.id == gpId) {
        app.viewerSalvos = gameplayer.player.id;
        app.viewer = gameplayer.player.email;
    } else {
        app.oponenteSalvos = gameplayer.player.id;
        app.oponente = gameplayer.player.email;
    }
}
// LOGOUT
function logout(evt) {
    evt.preventDefault();
    $.post("/api/logout", function () {
        location.href="http://localhost:8080/web/games.html";
    })
        .fail(function(error){
            alert("ERROR - USERNAME NO ALREADY EXIST");
            console.log(error);
        });
}


