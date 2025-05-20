package com.evaluation.register.register_api.controller;

import com.evaluation.register.register_api.model.entities.User;
import com.evaluation.register.register_api.model.form.LoginForm;
import com.evaluation.register.register_api.model.form.LoginResponse;
import com.evaluation.register.register_api.model.form.UserForm;
import com.evaluation.register.register_api.services.JwtService;
import com.evaluation.register.register_api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @Operation(summary = "Obtiene todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios encontrada"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        Iterable<User> users;
        users = this.userService.findAll();
        if (users == null){
            return ResponseEntity.internalServerError().build();
        }
        if (!users.iterator().hasNext()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(this.userService.findAll());
    }

    @Operation(summary = "Obtiene un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.userService.find(id));
    }

    @Operation(summary = "Crea un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado")
    })
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserForm userForm) {
        return ResponseEntity.ok().body(this.userService.create(userForm));
    }

    @Operation(summary = "Autentica un usuario y retorna un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginForm loginForm) {
        User authenticatedUser = userService.authenticate(loginForm);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        userService.update(authenticatedUser.getId(), jwtToken);
        return ResponseEntity.ok(loginResponse);
    }

}
