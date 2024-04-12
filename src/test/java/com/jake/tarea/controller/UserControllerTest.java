package com.jake.tarea.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jake.tarea.model.User;
import com.jake.tarea.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    Date customCreatedDate = new Date();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;
    private User user;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
        objectMapper = new ObjectMapper();

        user = User.builder()
                .id(1L)
                .name("Elsa")
                .email("algo@algo.com")
                .password("Almenos8caracteres")
                .created(new Date())
                .modified(new Date())
                .lastLogin(new Date())
                .uuid(String.valueOf(UUID.randomUUID()))
                .isActive(true)
                .build();
    }

    @Test
    public void saveLocal() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenReturn(user);

        // Realizar la solicitud POST y verificar el estado y el contenido de la respuesta
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.created").exists()) // Verificar que existe, pero no verificar el valor específico
                .andExpect(jsonPath("$.modified").exists())
                .andExpect(jsonPath("$.lastLogin").exists())
                .andExpect(jsonPath("$.uuid").value(user.getUuid())) // Comparar con el UUID del objeto user
                .andExpect(jsonPath("$.isActive").value(user.isActive()));
    }

    @Test
    public void getAllUsersTest() throws Exception {
        // Mockear los datos de usuario que se devolverán
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .id(1L)
                .name("jake")
                .email("jake@example.com")
                .build());
        users.add(User.builder()
                .id(2L)
                .name("kathy")
                .email("kathy@example.com")
                .build());

        // Configurar el comportamiento del userService.getAllUsers() para devolver la lista de usuarios mockeados
        when(userService.getAllUsers()).thenReturn(users);

        // Realizar la solicitud GET y verificar la respuesta
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(users.size())) // Verificar la longitud de la lista
                .andExpect(jsonPath("$[0].id").value(users.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(users.get(0).getName()))
                .andExpect(jsonPath("$[0].email").value(users.get(0).getEmail()))
                .andExpect(jsonPath("$[1].id").value(users.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(users.get(1).getName()))
                .andExpect(jsonPath("$[1].email").value(users.get(1).getEmail()));

        // Verificar que el método userService.getAllUsers() se haya llamado una vez
        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
    }
}
