package com.evaluation.register.register_api.model.form;

import com.evaluation.register.register_api.model.entities.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {
    private Long id;
    @NotEmpty(message = "debe registrar un email")
    @NotNull
    @Size(min = 6, message = "debe tener al menos 6 caracteres")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{3,}$", message = "Email debe tener formato user@domain.co")
    private String email;
    @NotEmpty(message = "debe registrar un nombre")
    @NotNull
    private String name;
    @NotEmpty(message = "debe registrar una contraseña")
    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?_&]{8,}$",
            message = "debe tener al menos 8 caracteres, una minúscula una mayúscula y un simbolo (@$!%*?_&)")
    private String password;
    private Set<Phone> phones;
}
