package com.jake.tarea.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private Date created;
    private Date modified;
    private Date lastLogin;
    private String uuid;
    private boolean isActive;
}
