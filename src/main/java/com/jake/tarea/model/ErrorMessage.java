package com.jake.tarea.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessage {
    private String mensaje;

    public ErrorMessage(String mensaje) {
        this.mensaje = mensaje;
    }

}
