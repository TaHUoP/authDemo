package authDemo.unit;

import authDemo.config.AppConfig;
import authDemo.controllers.api.CreateUserRequest;
import authDemo.controllers.api.UpdateUserRequest;
import authDemo.models.User;
import authDemo.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes= AppConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;

    @MockBean
    private CreateUserRequest createUserRequest;

    @MockBean
    private UpdateUserRequest updateUserRequest;

    private final ObjectId wrongId = new ObjectId("5e1f29ae42321d1cb2764af4");
    private final ObjectId id = new ObjectId("5e1b6af33ba81330a01ac41a");
    private final String name = "test";
    private final String email = "test@test.com";

    @Test
    public void testGetUsers() throws Exception {
        User user = new User(id, name, name, email);

        when(repository.findAll()).thenReturn(Arrays.asList(user));

        this.mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{\"id\":\"" + id + "\",\"firstName\":\"" + name + "\",\"lastName\":\"" + name + "\",\"email\":\"" + email + "\"}]"));
    }

    @Test
    public void testGetUsersEmpty() throws Exception {
        when(repository.findAll()).thenReturn(Arrays.asList());

        this.mockMvc.perform(get("/users"))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testGetUser() throws Exception {
        User user = new User(id, name, name, email);
        when(repository.findById(id)).thenReturn(Optional.of(user));

        this.mockMvc.perform(get("/users/" + id))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"id\":\"" + id + "\",\"firstName\":\"" + name + "\",\"lastName\":\"" + name + "\",\"email\":\"" + email + "\"}"));
    }

    @Test
    public void testGetUserWithWrongId() throws Exception {
        when(repository.findById(wrongId)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/users/" + wrongId))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(repository.deleteUserById(id)).thenReturn(1L);

        this.mockMvc.perform(delete("/users/" + id))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    public void testDeleteUserWithWrongId() throws Exception {
        when(repository.deleteUserById(wrongId)).thenReturn(0L);

        this.mockMvc.perform(delete("/users/" + wrongId))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUser() throws Exception {
        User newUser = new User(name, name, email);
        User user = new User(id, name, name, email);
        when(createUserRequest.createUser()).thenReturn(newUser);
        when(repository.save(newUser)).thenReturn(user);

        this.mockMvc.perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"firstName\":\"" + name + "\",\"lastName\":\"" + name + "\",\"email\":\"" + email + "\"}")
            )
            .andExpect(status().isCreated())
            .andExpect(content().json("{\"id\":\"" + id + "\",\"firstName\":\"" + name + "\",\"lastName\":\"" + name + "\",\"email\":\"" + email + "\"}"));
    }

    @Test
    public void testCreateUserWithInvalidRequestParams() throws Exception {
        this.mockMvc.perform(post("/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User(id, name, name, email);
        User updatedUser = new User(id, "new" + name, "new" + name, "new" + email);
        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(updatedUser);

        this.mockMvc.perform(
                put("/users/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"firstName\":\"new" + name + "\",\"lastName\":\"new" + name + "\",\"email\":\"new" + email + "\"}")
            )
            .andExpect(status().isOk())
            .andExpect(content().json("{\"id\":\"" + id + "\",\"firstName\":\"new" + name + "\",\"lastName\":\"new" + name + "\",\"email\":\"new" + email + "\"}"));
    }

    @Test
    public void testUpdateUserWithWrongId() throws Exception {
        when(repository.findById(wrongId)).thenReturn(Optional.empty());

        this.mockMvc.perform(
                put("/users/" + wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"new" + name + "\",\"lastName\":\"new" + name + "\",\"email\":\"new" + email + "\"}")
            )
            .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUserWithInvalidRequestParams() throws Exception {
        this.mockMvc.perform(put("/users/" + id))
                .andExpect(status().isBadRequest());
    }
}