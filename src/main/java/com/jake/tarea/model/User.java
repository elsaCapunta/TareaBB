package com.jake.tarea.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.*;



@Getter
@Setter
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank(message = "El campo 'name' es obligatorio")
    private String name;

    @NotNull
    @NotBlank(message = "El campo 'email' es obligatorio")
    @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida")
    private String email;

    @NotNull
    @NotBlank(message = "El campo 'password' es obligatorio")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "El formato del campo 'password' no es válido") //al menos 8 caracteres, incluyendo al menos una letra mayúscula, una letra minúscula y un número
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, updatable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified")
    private Date modified;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    @UpdateTimestamp
    private Date lastLogin;

    @Column(name="isActive")
    private boolean isActive;

    @PrePersist
    protected void onCreate() {
        created = new Date();
        modified = new Date();
    }

   @OneToMany(cascade = CascadeType.ALL)
   @JoinColumn(name="user_id")
    private List<Phone> phones = new ArrayList<>();

    public User() {
        this.isActive = true;
    }
}