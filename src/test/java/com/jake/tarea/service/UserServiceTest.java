package com.jake.tarea.service;

import com.jake.tarea.model.User;
import com.jake.tarea.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User local = User.builder()
                .id(1L)
                .name("Elsa")
                .email("algo@algo.com")
                .password("Almenos8caracteres")
                .build();
        Mockito.when(userRepository.findByNameIgnoreCase("Elsa")).thenReturn(Optional.of(local));
    }
}