package com.evaluation.register.register_api.services;

import com.evaluation.register.register_api.exceptions.ResourceNotFoundException;
import com.evaluation.register.register_api.model.entities.User;
import com.evaluation.register.register_api.model.form.ResponseForm;
import com.evaluation.register.register_api.model.form.UserForm;
import com.evaluation.register.register_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public boolean remove(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
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

    private User mappingToEntity(UserForm user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(user.getPassword());
        newUser.setPhones(user.getPhones());
        newUser.setToken("token");
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

}
