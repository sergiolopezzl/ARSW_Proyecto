package eci.arsw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  // Agrega este constructor por defecto
public class ChatMessage {
    String message;
    String user;
}
