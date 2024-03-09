$(document).ready(function() {
    // Función para cargar todas las subastas al cargar la página
    function cargarSubastas() {
            $.get("/api", function(data) {
                $("#subastas-list").empty();
                data.forEach(function(subasta) {
                    var subastaItem = "<li>Nombre: " + subasta.nombre + " - Descripción: " + subasta.descripcion +
                                      " - Usuario: " + subasta.usuario + " - Cantidad: " + subasta.cantidad +
                                      " - Evento: " + subasta.evento + " - Precio Inicial: $" + subasta.precioInicial +
                                      " - Precio Actual: $" + subasta.precioActual + " - Activa: " + subasta.activa + "</li>";
                    $("#subastas-list").append(subastaItem);
                });
            });
        }

    // Llamamos a la función cargarSubastas al cargar la página para obtener las subastas existentes
    cargarSubastas();

    // Evento para enviar el formulario de crear subasta
    $("#crear-subasta-form").submit(function(event) {
        event.preventDefault();
        var nombre = $("#nombre").val();
        var descripcion = $("#descripcion").val();
        var usuario = $("#usuario").val();
        var cantidad = parseFloat($("#cantidad").val());
        var evento = $("#evento").val();
        var precioInicial = parseFloat($("#precioInicial").val());
        var precioActual = parseFloat($("#precioActual").val());
        var activa = $("#activa").is(":checked");

        var nuevaSubasta = {
            nombre: nombre,
            descripcion: descripcion,
            usuario: usuario,
            cantidad: cantidad,
            evento: evento,
            precioInicial: precioInicial,
            precioActual: precioActual,
            activa: activa
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
