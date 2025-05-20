package com.evaluation.register.register_api.services;

import com.evaluation.register.register_api.exceptions.ResourceNotFoundException;
import com.evaluation.register.register_api.model.entities.User;
import com.evaluation.register.register_api.model.form.LoginForm;
import com.evaluation.register.register_api.model.form.ResponseForm;
import com.evaluation.register.register_api.model.form.UserForm;
import com.evaluation.register.register_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseForm create(UserForm user) {
        if (Boolean.TRUE.equals(this.userRepository.existsByEmail(user.getEmail()))){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Correo ya registrado");
        } else {
            User newUser = mappingToEntity(user);
            return reportBack(userRepository.save(newUser));
        }
    }


    private ResponseForm reportBack(User user) {
            ResponseForm form = new ResponseForm();
            form.setId(user.getId());
            form.setIsactive(user.getIsActive());
            form.setToken(user.getToken());
            form.setLastLogin(null);
            form.setCreated(user.getCreated());
            form.setModified(user.getModified());
            return form;

    }


    public Iterable<User> findAll() {
        Iterable<User> users = userRepository.findAll();
        if (!users.iterator().hasNext()){
            throw new ResourceNotFoundException("Listado de Usuarios");
        }
        return userRepository.findAll();
    }

    public UserForm find(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Usuario", "id", id);
        }
        return mappingToUserForm(user.get());
    }

    public UserForm update(long id, String token) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Usuario", "id", id);
        }
        User userToUpdate = user.get();
        userToUpdate.setToken(token);
        userToUpdate.setLastLogin(new Date());
        userToUpdate.setModified(new Date());
        return mappingToUserForm(userRepository.save(userToUpdate));
    }

    private User mappingToEntity(UserForm user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPhones(user.getPhones());
        newUser.setToken("");
        newUser.setIsActive(true);
        return newUser;
    }

    private UserForm mappingToUserForm(User user) {
        UserForm form = new UserForm();
        form.setId(user.getId());
        form.setEmail(user.getEmail());
        form.setName(user.getName());
        form.setPassword(user.getPassword());
        form.setPhones(user.getPhones());
        return form;
    }

    public User authenticate(LoginForm input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

}
