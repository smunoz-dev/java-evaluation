package com.evaluation.register.register_api.services;

import com.evaluation.register.register_api.exceptions.ResourceNotFoundException;
import com.evaluation.register.register_api.model.entities.User;
import com.evaluation.register.register_api.model.entities.UserAudit;
import com.evaluation.register.register_api.model.form.ResponseForm;
import com.evaluation.register.register_api.model.form.UserForm;
import com.evaluation.register.register_api.repository.UserRepository;
import com.evaluation.register.register_api.util.Operations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
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
            return reportBack(userRepository.save(newUser), Operations.CREATE);
        }
    }

    private ResponseForm reportBack(User user, int operation) {
        if (operation == Operations.CREATE) {
            UserAudit userAudit = new UserAudit();
            userAudit.setId(user.getId());
            userAudit.setCreated(new Date());
            userAudit.setModified(new Date());
            userAudit.setLastLogin(new Date());
            userAudit.setToken("token1");
            userAudit.setIsActive(true);
            return mappingToResponseForm(userAudit);
        }
      return null;
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
        return newUser;
    }
    private ResponseForm mappingToResponseForm(UserAudit user) {
        ResponseForm form = new ResponseForm();
        form.setCreated(user.getCreated());
        form.setModified(user.getModified());
        form.setId(user.getId());
        form.setLastLogin(user.getLastLogin());
        form.setToken(user.getToken());
        form.setIsactive(user.getIsActive());
        return form;
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
