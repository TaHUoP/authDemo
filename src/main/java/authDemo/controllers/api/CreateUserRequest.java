package authDemo.controllers.api;

import authDemo.models.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CreateUserRequest {

    @NotBlank(message = "User firstName must not be blank")
    private String firstName;

    @NotBlank(message = "User lastName must not be blank")
    private String lastName;

    @NotBlank(message = "User email must not be blank")
    @Email(message = "User email must be compatible with RFC-5322")
    private String email;

    public User createUser() {
        return new User(firstName, lastName, email);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
