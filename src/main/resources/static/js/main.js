$( document ).ready(function() {
    $(".sidebar .nav-link").click(function (){
        $(this).children(".plus").css("transition", "0.5s")
        $(this).parent().children(".collapse").slideToggle();
        $(this).parent().toggleClass("active");
    });

    var currentUrl = window.location.pathname;

    var urlPath = currentUrl.split("?")[0];
    var parts = urlPath.split("/").filter(part => part !== "");

    if (parts[0] === "") {
        parts.shift();
    }

    console.log(currentUrl)
    console.log(parts.length)

    if (parts.length == 3) {
        var tab = parts[parts.length - 2];
        console.log(tab);
        $("#" + tab).addClass("active");
        $("#" + tab).children(".collapse").css("display", "block");

        var lastParam = parts[parts.length - 1];
        console.log(lastParam)

        $("#" + tab).find(".collapse-item").each(function(){

            if($(this).attr("sub_tab") == lastParam){
                $(this).addClass("active");
            }
        });
    }

    if (parts.length == 2){
        var tab = parts[parts.length - 2];
        console.log(tab);
        $("#" + tab).addClass("active");
        $("#" + tab).children(".collapse").css("display", "block");

        var lastParam = parts[parts.length - 1];
        console.log(lastParam)

        $("#" + tab).find(".collapse-item").each(function(){

            if($(this).attr("sub_tab") == lastParam){
                $(this).addClass("active");
            }
        });
    }

    if(parts.length == 0){

        $("#home").addClass("active");
    }

    $(".filter-content .active-item").click(function (){
        $(".filter-content .filter").slideToggle();
    })

});