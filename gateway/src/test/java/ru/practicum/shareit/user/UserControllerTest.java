package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    private UserClient client;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private final String requestHeader = "X-Sharer-User-Id";
    private UserDto userDto1 = new UserDto();
    private String name1 = "name1";
    private String email1 = "email1@example.com";
    private long userId = 1;
    private String baseUri = "/users";

    @BeforeEach
    void setUp() {
        userDto1.setName(name1);
        userDto1.setEmail(email1);
    }

    ResultActions performMvc(MockHttpServletRequestBuilder builder) throws Exception {
        return mvc.perform(builder);
    }

    MockHttpServletRequestBuilder setRequestHeaders(MockHttpServletRequestBuilder builder) throws JsonProcessingException {
        return builder
                .content(mapper.writeValueAsString(userDto1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON);
    }

    @Test
    void create() throws Exception {
        performMvc(setRequestHeaders(post(baseUri)))
                .andExpect(status().isCreated());
    }

    @Test
    void createWithId() throws Exception {
        userDto1.setId(userId);

        performMvc(setRequestHeaders(post(baseUri)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithoutEmail() throws Exception {
        userDto1.setEmail(null);

        performMvc(setRequestHeaders(post(baseUri)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserInvalidEmail() throws Exception {
        userDto1.setEmail("email1.com");

        performMvc(setRequestHeaders(post(baseUri)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update() throws Exception {
        performMvc(setRequestHeaders(patch(baseUri + "/" + userId)))
                .andExpect(status().isOk());
    }

    @Test
    void updateWithoutEmail() throws Exception {
        userDto1.setEmail(null);

        performMvc(setRequestHeaders(patch(baseUri + "/" + userId)))
                .andExpect(status().isOk());
    }

    @Test
    void updateWithoutName() throws Exception {
        userDto1.setName(null);

        performMvc(setRequestHeaders(patch(baseUri + "/" + userId)))
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        mvc.perform(get(baseUri + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findAll() throws Exception {
        mvc.perform(get(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete(baseUri + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}