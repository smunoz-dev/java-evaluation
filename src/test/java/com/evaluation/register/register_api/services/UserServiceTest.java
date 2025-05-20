package com.evaluation.register.register_api.services;

import com.evaluation.register.register_api.exceptions.ResourceNotFoundException;
import com.evaluation.register.register_api.model.entities.User;
import com.evaluation.register.register_api.model.form.LoginForm;
import com.evaluation.register.register_api.model.form.ResponseForm;
import com.evaluation.register.register_api.model.form.UserForm;
import com.evaluation.register.register_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, authenticationManager, passwordEncoder);
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Lanza excepción cuando se intenta crear usuario con email ya registrado")
    void lanzaExcepcionCuandoEmailYaRegistrado() {
        UserForm userForm = new UserForm();
        userForm.setEmail("test@mail.com");
        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.create(userForm));
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Crea usuario correctamente cuando email no está registrado")
    void creaUsuarioCorrectamenteCuandoEmailNoRegistrado() {
        UserForm userForm = new UserForm();
        userForm.setEmail("nuevo@mail.com");
        userForm.setName("Nuevo");
        userForm.setPassword("1234");
        when(userRepository.existsByEmail("nuevo@mail.com")).thenReturn(false);
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            user.setCreated(new java.util.Date());
            user.setModified(new java.util.Date());
            return user;
        });

        ResponseForm response = userService.create(userForm);

        assertNotNull(response.getId());
        assertTrue(response.isIsactive());
        assertEquals("", response.getToken());
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Lanza excepción cuando no hay usuarios en findAll")
    void lanzaExcepcionCuandoNoHayUsuariosEnFindAll() {
        Iterable<User> emptyUsers = java.util.Collections::emptyIterator;
        when(userRepository.findAll()).thenReturn(emptyUsers);

        assertThrows(ResourceNotFoundException.class, () -> userService.findAll());
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Devuelve lista de usuarios cuando existen usuarios en findAll")
    void devuelveListaUsuariosCuandoExistenUsuariosEnFindAll() {
        User user = new User();
        Iterable<User> users = java.util.List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        Iterable<User> result = userService.findAll();

        assertTrue(result.iterator().hasNext());
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Lanza excepción cuando no encuentra usuario por id")
    void lanzaExcepcionCuandoNoEncuentraUsuarioPorId() {
        when(userRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.find(99L));
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Devuelve UserForm cuando encuentra usuario por id")
    void devuelveUserFormCuandoEncuentraUsuarioPorId() {
        User user = new User();
        user.setId(1L);
        user.setEmail("a@mail.com");
        user.setName("A");
        user.setPassword("pass");
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        UserForm result = userService.find(1L);

        assertEquals(1L, result.getId());
        assertEquals("a@mail.com", result.getEmail());
    }


    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Lanza excepción cuando actualiza usuario inexistente")
    void lanzaExcepcionCuandoActualizaUsuarioInexistente() {
        when(userRepository.findById(100L)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.update(100L, "token"));
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Autentica usuario correctamente")
    void autenticaUsuarioCorrectamente() {
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("c@mail.com");
        loginForm.setPassword("pass");
        User user = new User();
        user.setEmail("c@mail.com");
        when(userRepository.findByEmail("c@mail.com")).thenReturn(java.util.Optional.of(user));

        User result = userService.authenticate(loginForm);

        assertEquals("c@mail.com", result.getEmail());
    }
}