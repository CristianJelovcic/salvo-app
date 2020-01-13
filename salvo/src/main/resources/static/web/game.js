const urlParams = new URLSearchParams(window.location.search);
const gpId = urlParams.get("gp");
var grid;
var currentUser;

//GET JSON
$(function () {
    function loadData() {
        $.get("/api/game_view/" + gpId)
            .done(function (data) {
                myJson(data);
            })
            .fail(function (jqXHR, textStatus) {
                console.log("Failed: " + textStatus);
            });
    };
    loadData();
});


//------------------------------------------ VUE OBJECT ----------------------------------------------------------------------------

var app = new Vue({
    el: '#app',
    data: {
        game_view: {},
        coordenadaX: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
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
        newSalvo:{turn: 0, locationSalvo: []},
        numberOfShots:5,
        shoots:[],
        hits: [],
        placedShips: []
    },
    methods:{
        vshoot: function(id) {
            shoot(id);
}
    }
})
//------------------------------------------ JSON  ---------------------------------------------------------------------

// TRANSFER BACKEND DATA
function myJson(data) {
    var myJson = data;
    currentUser = data;
    app.game_view = myJson;
    app.gamePlayer = myJson.gamePlayers;
    app.ships = myJson.ships;
    app.salvoes = myJson.salvos;
    selectShips(app.ships);
    listGamePlayers(app.gamePlayer);
    selectSalvos(app.salvoes);
    createGrid();

}

//------------------------------------------   GRID  -------------------------------------------------------------------

function createGrid() {
    var options = {
        //grilla de 10 x 10
        width: 10,
        height: 10,
        //separacion entre elementos (les llaman widgets)
        verticalMargin: 0,
        //altura de las celdas
        cellHeight: 45,
        //desabilitando el resize de los widgets
        disableResize: true,
        //widgets flotantes
        float: true,
        //removeTimeout: 100,
        //permite que el widget ocupe mas de una columna
        disableOneColumnMode: true,
        //false permite mover, true impide
        staticGrid: 0,
        //activa animaciones (cuando se suelta el elemento se ve más suave la caida)
        animate: true
    }

    // START THE GRID
    $('.grid-stack').gridstack(options);
    grid = $('#grid').data('gridstack');

    // condicion se no hay barcos  Si verdadero =(crea barcos y posiciones) falso=(crea y posiciona desde el Json)
    if (app.game_view.ships.length === 0) {
        options.staticGrid = false;

        createShips();

        $('.grid-stack').gridstack(options);
        grid = $('#grid').data('gridstack');


        $("#carrier").click(function () {
            if ($(this).children().hasClass("carrier-Horizontal")) {
                grid.resize($(this), 1, 5);
                $(this).children().removeClass("carrier-Horizontal");
                $(this).children().addClass("carrier-Vertical");
            } else {
                grid.resize($(this), 5, 1);
                $(this).children().addClass("carrier-Horizontal");
                $(this).children().removeClass("carrier-Vertical");
            }
        });

        $("#patroal").click(function () {
            if ($(this).children().hasClass("patroal-Horizontal")) {
                grid.resize($(this), 1, 2);
                $(this).children().removeClass("patroal-Horizontal");
                $(this).children().addClass("patroal-Vertical");
            } else {
                grid.resize($(this), 2, 1);
                $(this).children().addClass("patroal-Horizontal");
                $(this).children().removeClass("patroal-Vertical");
            }
        });
        $("#submarine").click(function () {
            if ($(this).children().hasClass("submarine-Horizontal")) {
                grid.resize($(this), 1, 3);
                $(this).children().removeClass("submarine-Horizontal");
                $(this).children().addClass("submarine-Vertical");
            } else {
                grid.resize($(this), 3, 1);
                $(this).children().addClass("submarine-Horizontal");
                $(this).children().removeClass("submarine-Vertical");
            }
        });
        $("#destroyer").click(function () {
            if ($(this).children().hasClass("destroyer-Horizontal")) {
                grid.resize($(this), 1, 3);
                $(this).children().removeClass("destroyer-Horizontal");
                $(this).children().addClass("destroyer-Vertical");
            } else {
                grid.resize($(this), 3, 1);
                $(this).children().addClass("destroyer-Horizontal");
                $(this).children().removeClass("destroyer-Vertical");
            }
        });
        $("#battleship").click(function () {
            if ($(this).children().hasClass("battleship-Horizontal")) {
                grid.resize($(this), 1, 4);
                $(this).children().removeClass("battleship-Horizontal");
                $(this).children().addClass("battleship-Vertical");
            } else {
                grid.resize($(this), 4, 1);
                $(this).children().addClass("battleship-Horizontal");
                $(this).children().removeClass("battleship-Vertical");
            }
        });


    } else {
        options.staticGrid = true;
        grid = $('#grid').data('gridstack');
        //createShips()
        getShipsLocation();
    }
}



//------------------------------------------  SHIPS  ----------------------------------------------------------------------------
// SEND A POST WITH AN OBJECT VUE FROM SHIPS TO THE BACKEND
function postShips() {

    $.post({
        url: "/api/games/players/" + gpId + "/ships",
        data: JSON.stringify(app.placedShips),
        dataType: "text",
        contentType: "application/json"
    })
        .done(function () {
            window.location.reload();
            console.log("done");
        })
        .fail(function () {
            console.log("fail");
        })
}

// BUILD LOCATION FOR EACH SHIP
$("#startPlay-btn").click(function () {
    $(".grid-stack-item").each(function () {
        var coordinate = [];
        var ship = {typeShip: "", locationShip: ""};

        if ($(this).attr("data-gs-width") !== "1") {
            for (var i = 0; i < parseInt($(this).attr("data-gs-width")); i++) {
                coordinate.push(String.fromCharCode(parseInt($(this).attr("data-gs-y")) + 65) + (parseInt($(this).attr("data-gs-x")) + i + 1).toString());
            }
        } else {
            for (var i = 0; i < parseInt($(this).attr("data-gs-height")); i++) {
                coordinate.push(String.fromCharCode(parseInt($(this).attr("data-gs-y")) + i + 65) + (parseInt($(this).attr("data-gs-x")) + 1).toString());
            }
        }

        ship.typeShip = $(this).children().attr("alt");
        ship.locationShip = coordinate;
        app.placedShips.push(ship);
    })
    postShips();
});

// CREATE WIDGETS (SHIPS)
function createShips() {

    grid.addWidget($('<div id="carrier"><div class="grid-stack-item-content carrier-Horizontal" alt="carrier"></div></div>'),
        0, 0, 5, 1);

    grid.addWidget($('<div id="patroal"><div class="grid-stack-item-content patroal-Horizontal" alt="patroal"></div></div>'),
        0, 4, 2, 1);

    grid.addWidget($('<div id="submarine"><div class="grid-stack-item-content submarine-Horizontal" alt="submarine"></div></div>'),
        0, 3, 3, 1);

    grid.addWidget($('<div id="destroyer"><div class="grid-stack-item-content destroyer-Horizontal" alt="destroyer"></div></div>'),
        0, 2, 3, 1);

    grid.addWidget($('<div id="battleship"><div class="grid-stack-item-content battleship-Horizontal" alt="battleship"></div></div>'),
        0, 1, 4, 1);

}

// GET THE POSITIONS FROM DATA
function getShipsLocation() {
    for (var i in currentUser.ships) {
        let gsx = parseInt(currentUser.ships[i].location[0].slice(1)) - 1;
        let gsy = parseInt(currentUser.ships[i].location[0].slice(0, 1).charCodeAt(0)) - 65;


        // este if evalua si es veertical la posicion del widget
        if (currentUser.ships[i].location[0].charAt(0) !== currentUser.ships[i].location[1].charAt(0)) {
            let height = currentUser.ships[i].location.length;
            let width = 1;
            grid.addWidget(('<div id="' + currentUser.ships[i].type + '"><div class="grid-stack-item-content ' + currentUser.ships[i].type + '-Vertical"></div></div>'),
                gsx, gsy, width, height);
        } else {
            let height = 1;
            let width = currentUser.ships[i].location.length;
            grid.addWidget(('<div id="' + currentUser.ships[i].type + '"><div class="grid-stack-item-content ' + currentUser.ships[i].type + '-Horizontal"></div></div>'),
                gsx, gsy, width, height);
        }
    }
}

// SELECT THE SHIP
function selectShips(ships) {
    ships.forEach(selectLocations);
}


// SELECT THE LOCACION
function selectLocations(ship) {
    ship.location.forEach(loc => paintLocation(loc, ship.type));

}

// PAINT THE CELL ACCORDING TO THE VALUE OF THE LOCATION LOCATION [I]
function paintLocation(locationShip) {
    var elemento = document.getElementById(locationShip);
    //elemento.classList.add("ship");
}

//------------------------------------------ HITS (SALVOS) -------------------------------------------------------------

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
// PAINT THE SELDAS WITH IMPACT OF SAVINGS ON SHIPS
function paintLocationSalvoHits(loc, turn) {
    var elemento = document.getElementById(loc);
    elemento.innerHTML = turn;
    elemento.classList.remove("ship");
    elemento.classList.add("salvosHits");
}

//------------------------------------------ SALVOS ----------------------------------------------------------------------------
var MESSAGE_ERROR={};
// POST DEL SALVO
function addSalvoes(){
    $.post({
        url: "/api/games/players/"+gpId+"/salvoes",
        data: JSON.stringify(app.newSalvo),
        dataType: "text",
        contentType: "application/json"})
        .done(function(){
            console.log("done");
            window.location.reload();
        })
        .fail(function(jqXHR, textStatus, errorThrown){
            MESSAGE_ERROR=jqXHR.responseText;
            alert(MESSAGE_ERROR);
            console.log(MESSAGE_ERROR);
            window.location.reload();
        })
}


// FUNCION PARA EL CLICK EN LA CELDA


// FUNCION PARA CAPTURAR LOS VALORES DEL SALVO
function shoot(event) {
    let id = event.target.id.slice(1,3);
    console.log(event.target);
    if (!document.body.classList.contains('salvos')){
        if (app.newSalvo.locationSalvo.length<app.numberOfShots){
            app.newSalvo.locationSalvo.push(id);
            console.log("SALVO: "+app.newSalvo.locationSalvo);
            app.shoots.push(id);
            console.log("LISTA DE DIPAROS: "+ app.shoots);
            event.target.classList.add("salvos");
            //app.newSalvo.locationSalvo.length===5? window.location.reload():"";
        }else{
            addSalvoes();
            app.newSalvo.locationSalvo=[];
        }
    }else {
        alert("YOU CAN'T SHOOT HERE")
    }

}



// SELECT THE SAVED
function selectSalvos(salvos) {
    salvos.forEach(loc => selectLocationsSalvos(loc));
}

function selectLocationsSalvos(salvo) {
    if (salvo.player == app.viewerSalvos) {
        salvo.salvoLocation.forEach(loc => paintLocationSalvo(loc, salvo.turn));
    } else {
        salvoHits(salvo);
    }

}

// PAINT THE CELL ACCORDING TO THE VALUE OF THE LOCATION LOCATION [I]
function paintLocationSalvo(location, turn) {
    var elemento = document.getElementById('s' + location);
    elemento.innerHTML = turn;
    elemento.classList.add("salvos");
}

//------------------------------------------ VISTA JUGADOR ----------------------------------------------------------------------------

// SHOW THE PLAYERS IN GAME VIEW
function listGamePlayers(gameplayers) {
    gameplayers.forEach(toShowPlayer)
}

// CONDITION TO SHOW THE VIEWER SHIPS
function toShowPlayer(gameplayer) {
    if (gameplayer.id == gpId) {
        app.viewerSalvos = gameplayer.player.id;
        app.viewer = gameplayer.player.email;
    } else {
        app.oponenteSalvos = gameplayer.player.id;
        app.oponente = gameplayer.player.email;
    }
}

//------------------------------------------ CONTROLES ----------------------------------------------------------------------------

// LOGOUT
function logout() {
    $.post("/api/logout", function () {
        location.href = "http://localhost:8080/web/games.html";
    })
        .fail(function (error) {
            alert("ERROR - USERNAME NO ALREADY EXIST");
            console.log(error);
        });
}


