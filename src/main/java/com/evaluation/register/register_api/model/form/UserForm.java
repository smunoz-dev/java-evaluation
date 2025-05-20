package com.evaluation.register.register_api.model.form;

import com.evaluation.register.register_api.model.entities.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {
    private Long id;
    @NotEmpty
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
    private Set<Phone> phones;
}
