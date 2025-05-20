package com.evaluation.register.register_api.controller;

import com.evaluation.register.register_api.model.form.MessageForm;
import com.evaluation.register.register_api.model.form.ResponseForm;
import com.evaluation.register.register_api.model.form.UserForm;
import com.evaluation.register.register_api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        List<UserForm> users;
        users = this.userService.getAllUsers();
        if (users == null){
            return ResponseEntity.internalServerError().build();
        }
        if (users.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long id) {
        Optional<UserForm> user;
        user = this.userService.find(id);
        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new MessageForm("Usuario no encontrado con id: " + id));
        }
        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserForm userForm) {
        if (this.userService.existsByEmail(userForm)){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MessageForm("Correo ya registrado"));
        } else {
            ResponseForm response = this.userService.create(userForm);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageForm("Error al crear el usuario"));
        }

    }

    @DeleteMapping
    public ResponseEntity<Object> deleteUser(@RequestBody UserForm userForm) {
        return ResponseEntity.ok(this.userService.remove(userForm.getId()));
    }
}
