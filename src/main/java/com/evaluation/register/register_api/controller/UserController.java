package com.evaluation.register.register_api.controller;

import com.evaluation.register.register_api.model.entities.User;
import com.evaluation.register.register_api.model.form.UserForm;
import com.evaluation.register.register_api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.userService.find(id));
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserForm userForm) {
        return ResponseEntity.ok().body(this.userService.create(userForm));
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteUser(@RequestBody UserForm userForm) {
        return ResponseEntity.ok(this.userService.remove(userForm.getId()));
    }
}
