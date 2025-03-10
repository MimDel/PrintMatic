package com.example.printmatic.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RegistrationDTO implements Serializable {
    @NotNull(message = "email can't be null")
    @Email(message = "email is invalid")
    private String email;

    @NotNull
    @Size(min=8, max = 100)
    private String password;

    @NotNull
    @Size(min=3, max = 100, message = "First name should be between 3 - 100 symbols.")
    private String firstName;

    @NotNull
    @Size(min=3, max = 100, message = "Last name should be between 3 - 100 symbols.")
    private String lastName;

    @NotNull
    @Size(min=10, max=10, message = "The phoneNumber should be 10 digits.")
    private String phoneNumber;
}
