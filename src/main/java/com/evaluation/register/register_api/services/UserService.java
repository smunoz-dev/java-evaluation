package com.evaluation.register.register_api.services;

import com.evaluation.register.register_api.model.entities.User;
import com.evaluation.register.register_api.model.entities.UserAudit;
import com.evaluation.register.register_api.model.form.ResponseForm;
import com.evaluation.register.register_api.model.form.UserForm;
import com.evaluation.register.register_api.repository.UserRepository;
import com.evaluation.register.register_api.util.Operations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public List<UserForm> getAllUsers() {
        List<UserForm> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserForm user = new UserForm();
            user.setId(i + 1L);
            user.setEmail("test"+ i +"@test.com");
            user.setName("user" + i);
            user.setPassword("password1" + i);
            user.setPhones(new HashSet<>());
            users.add(user);
        }
        return users;
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseForm create(UserForm user) {
        User newUser = mappingToEntity(user);
        return reportBack(userRepository.save(newUser), Operations.CREATE);
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
            ResponseForm response = mappingToResponseForm(userAudit);
            return response;
        }
      return null;
    }
//
//    public UserForm update(UserForm user) {
//        return userRepository.save(user);
//    }
//
//    public UserForm change(UserForm user) {
//        return userRepository.save(user);
//    }

    public boolean remove(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<UserForm> find(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserForm form = mappingToUserForm(user.get());
            return Optional.of(form);
        }
        return Optional.empty();
    }

    public Boolean existsByEmail(UserForm userFormRequested) {
        return userRepository.existsByEmail(userFormRequested.getEmail());
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
