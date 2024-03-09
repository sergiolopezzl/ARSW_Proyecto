$(document).ready(function() {
    // Función para cargar todas las subastas al cargar la página
    function cargarSubastas() {
        $.get("/api", function(data) {
            $("#subastas-list").empty();
            data.forEach(function(subasta) {
                $("#subastas-list").append("<li>" + subasta.nombre + " - Precio: $" + subasta.precioActual + "</li>");
            });
        });
    }

    // Llamamos a la función cargarSubastas al cargar la página para obtener las subastas existentes
    cargarSubastas();

    // Evento para enviar el formulario de crear subasta
    $("#crear-subasta-form").submit(function(event) {
        event.preventDefault();
        var nombre = $("#nombre").val();
        var precio = $("#precio").val();
        var nuevaSubasta = {
            nombre: nombre,
            precioActual: precio
        };
        crearSubasta(nuevaSubasta);
    });

    // Función para crear una nueva subasta
    function crearSubasta(subasta) {
        $.ajax({
            type: "POST",
            url: "/api",
            contentType: "application/json",
            data: JSON.stringify(subasta),
            success: function(response) {
                console.log("Nueva subasta creada:", response);
                cargarSubastas(); // Volvemos a cargar las subasta después de crear una nueva
            },
            error: function(err) {
                console.error("Error al crear una nueva subasta:", err);
            }
        });
    }
});
