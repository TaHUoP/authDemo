package authDemo.controllers.api;

import authDemo.models.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

public class UpdateUserRequest {

    @Pattern(regexp = "^[\\S]+.*[\\S]+$", message = "User firstName must not be blank")
    private String firstName;

    @Pattern(regexp = "^[\\S]+.*[\\S]+$", message = "User lastName must not be blank")
    private String lastName;

    @Pattern(regexp = "^[\\S]+.*[\\S]+$", message = "User email must not be blank")
    @Email(message = "User email must be compatible with RFC-5322")
    private String email;

    public void populateUser(User user) {
        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (email != null) user.setEmail(email);
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
