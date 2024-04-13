package eci.arsw.controller;

import eci.arsw.model.Usuario;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public Usuario chat(@DestinationVariable String roomId, Usuario usuario) {
        System.out.println(usuario);
        // Aquí puedes realizar operaciones con el usuario recibido, como guardar en la base de datos, etc.
        return usuario; // Retornamos el usuario recibido de vuelta
    }
}
