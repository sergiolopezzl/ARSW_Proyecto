package eci.arsw.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class  ChatMessage {
    String message;
    String user;
}
