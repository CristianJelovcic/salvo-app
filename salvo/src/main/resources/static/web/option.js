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

    //agregando un elmento(widget) desde el javascript
    grid.addWidget($('<div id="carrier2"><div class="grid-stack-item-content carrierHorizontal"></div><div/>'),
        1, 5, 3, 1);

    grid.addWidget($('<div id="patroal2"><div class="grid-stack-item-content patroalHorizontal"></div><div/>'),
        1, 8, 2, 1);

    //verificando si un area se encuentra libre
    //no está libre, false
    console.log(grid.isAreaEmpty(1, 8, 3, 1));
    //está libre, true
    console.log(grid.isAreaEmpty(1, 7, 3, 1));

    $("#carrier,#carrier2").click(function(){
        if($(this).children().hasClass("carrierHorizontal")){
            grid.resize($(this),1,3);
            $(this).children().removeClass("carrierHorizontal");
            $(this).children().addClass("carrierHorizontalRed");
        }else{
            grid.resize($(this),3,1);
            $(this).children().addClass("carrierHorizontal");
            $(this).children().removeClass("carrierHorizontalRed");
        }
    });

    $("#patroal,#patroal2").click(function(){
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

    //todas las funciones se encuentran en la documentación
    //https://github.com/gridstack/gridstack.js/tree/develop/doc
});