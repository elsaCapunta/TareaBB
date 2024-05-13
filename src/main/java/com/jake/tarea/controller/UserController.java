package com.jake.tarea.controller;
import com.jake.tarea.jwt.JWTGenerator;
import com.jake.tarea.service.UserService;
import com.jake.tarea.dto.UserDTO;
import com.jake.tarea.model.ErrorMessage;
import com.jake.tarea.model.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("api/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    private  final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JWTGenerator jwtGenerator;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {

        if (userService.existsByEmail(user.getEmail())) {
            ErrorMessage errorMessage = new ErrorMessage("El correo ya registrado");
            return new ResponseEntity<>(errorMessage, HttpStatus.OK);
        }
        log.info("Clave encriptada: {} :",bCryptPasswordEncoder.encode(user.getPassword()));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User createUser = userService.createUser(user);
        UserDTO userDTO = new UserDTO();

        String token = jwtGenerator.getToken(user.getEmail());
        BeanUtils.copyProperties(createUser, userDTO);
        userDTO.setToken(token);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public User searchUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }

    @DeleteMapping("{id}")
    public void deleteUserById(@PathVariable("id") Long id){
        userService.deleteUser(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError.getDefaultMessage();
        String status = String.valueOf(ex.getBody().getStatus());

        ErrorMessage errorResponse = new ErrorMessage(errorMessage);

        if (status.equals("400")) {
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else if (status.equals("401")) {
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        } else if (status.equals("403")) {
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        } else if (status.equals("404")) {
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } else if (status.equals("405")) {
            return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
        } else if (status.equals("409")) {
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
