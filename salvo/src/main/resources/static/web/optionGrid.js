$(function () {
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
        staticGrid: false,
        //activa animaciones (cuando se suelta el elemento se ve más suave la caida)
        animate: true
    }
    //se inicializa el grid con las opciones
    $('.grid-stack').gridstack(options);

    grid = $('#grid').data('gridstack');












    // ESTE CODIGO AGRGO WIDGET DESDE JAVASCRIPT CON ADDWIDGET



    //agregando un elmento(widget) desde el javascript
/*    grid.addWidget($('<div id="carrier"><div class="grid-stack-item-content carrierHorizontal"></div><div/>'),
        0, 0, 5, 1);

    grid.addWidget($('<div id="patroal"><div class="grid-stack-item-content patroalHorizontal"></div><div/>'),
        0, 4, 2, 1);

    grid.addWidget($('<div id="submarine"><div class="grid-stack-item-content submarineHorizontal"></div><div/>'),
       0, 3, 3, 1);

    grid.addWidget($('<div id="destroyer"><div class="grid-stack-item-content destroyerHorizontal"></div><div/>'),
       0, 2, 3, 1);

    grid.addWidget($('<div id="battleship"><div class="grid-stack-item-content battleshipHorizontal"></div><div/>'),
       0, 1, 4, 1);*/
       




    //verificando si un area se encuentra libre
    //no está libre, false
    console.log(grid.isAreaEmpty(1, 8, 3, 1));
    //está libre, true
    console.log(grid.isAreaEmpty(1, 7, 3, 1));

    $("#carrier").click(function(){
        if($(this).children().hasClass("carrierHorizontal")){
            grid.resize($(this),1,5);
            $(this).children().removeClass("carrierHorizontal");
            $(this).children().addClass("carrierHorizontalRed");
        }else{
            grid.resize($(this),5,1);
            $(this).children().addClass("carrierHorizontal");
            $(this).children().removeClass("carrierHorizontalRed");
        }
    });

    $("#patroal").click(function(){
        if($(this).children().hasClass("patroalHorizontal")){
            grid.resize($(this),1,2);
            $(this).children().removeClass("patroalHorizontal");
            $(this).children().addClass("patroalHorizontalRed");
        }else{
            grid.resize($(this),2,1);
            $(this).children().addClass("patroalHorizontal");
            $(this).children().removeClass("patroalHorizontalRed");
        }
    });
    $("#submarine").click(function(){
        if($(this).children().hasClass("submarineHorizontal")){
            grid.resize($(this),1,3);
            $(this).children().removeClass("submarineHorizontal");
            $(this).children().addClass("submarineHorizontalRed");
        }else{
            grid.resize($(this),3,1);
            $(this).children().addClass("submarineHorizontal");
            $(this).children().removeClass("submarineHorizontalRed");
        }
    });
    $("#destroyer").click(function(){
        if($(this).children().hasClass("destroyerHorizontal")){
            grid.resize($(this),1,3);
            $(this).children().removeClass("destroyerHorizontal");
            $(this).children().addClass("destroyerHorizontalRed");
        }else{
            grid.resize($(this),3,1);
            $(this).children().addClass("destroyerHorizontal");
            $(this).children().removeClass("destroyerHorizontalRed");
        }
    });
    $("#battleship").click(function(){
        if($(this).children().hasClass("battleshipHorizontal")){
            grid.resize($(this),1,4);
            $(this).children().removeClass("battleshipHorizontal");
            $(this).children().addClass("battleshipHorizontalRed");
        }else{
            grid.resize($(this),4,1);
            $(this).children().addClass("battleshipHorizontal");
            $(this).children().removeClass("battleshipHorizontalRed");
        }
    });

var patroal= document.getElementById("patroal");











    // ESTO ES CODIGO VIEJO, LO DEJO PARA  FUTURAS PRUEBAS



    // console.log(grid.isAreaEmpty(1, 8, 3, 1));
    // //está libre, true
    // console.log(grid.isAreaEmpty(1, 7, 3, 1));

    // $("#carrier,#carrier2").click(function(){
    //     if($(this).children().hasClass("carrierHorizontal")){
    //         grid.resize($(this),1,3);
    //         $(this).children().removeClass("carrierHorizontal");
    //         $(this).children().addClass("carrierHorizontalRed");
    //     }else{
    //         grid.resize($(this),3,1);
    //         $(this).children().addClass("carrierHorizontal");
    //         $(this).children().removeClass("carrierHorizontalRed");
    //     }
    // });

    // $("#patroal,#patroal2").click(function(){
    //     if($(this).children().hasClass("patroalHorizontal")){
    //         grid.resize($(this),1,2);
    //         $(this).children().removeClass("patroalHorizontal");
    //         $(this).children().addClass("patroalHorizontalRed");
    //     }else{
    //         grid.resize($(this),2,1);
    //         $(this).children().addClass("patroalHorizontal");
    //         $(this).children().removeClass("patroalHorizontalRed");
    //     }
    // });

    //todas las funciones se encuentran en la documentación
    //https://github.com/gridstack/gridstack.js/tree/develop/doc
});