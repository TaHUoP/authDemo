/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package authDemo.controllers;

import authDemo.models.User;
import authDemo.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return repository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
    }

    @PostMapping("/users")
    public User createUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
        return repository.save(new User(firstName, lastName, email));
    }

    @PatchMapping("/users/{id}")
    public User updateUser(
        @PathVariable String id,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email
    ) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        user.firstName = firstName != null ? firstName : user.firstName;
        user.lastName = lastName != null ? lastName : user.lastName;
        user.email = email != null ? email : user.email;
        return repository.save(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable String id) {
        repository.deleteById(id);
    }

}