package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@DisabledInAotMode
class UserControllerTest {
    private final String baseUri = "/users";
    private final String idUri = "/";
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private UserDto dto = new UserDto(1L, "Qdsdq", "Qdsdq@gmail.com");

    ResultActions performMvcPost(String uri) throws Exception {
        return mvc.perform(setRequestHeaders(post(uri)));
    }

    ResultActions performMvcPatch(String uri) throws Exception {
        return mvc.perform(setRequestHeaders(patch(uri)));
    }

    ResultActions performMvcGet(String uri) throws Exception {
        return mvc.perform(setRequestHeaders(get(uri)));
    }

    MockHttpServletRequestBuilder setRequestHeaders(MockHttpServletRequestBuilder builder) throws JsonProcessingException {
        return builder.content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON);
    }

    @Test
    void save() throws Exception {
        when(userService.save(any()))
                .thenReturn(dto);
        performMvcPost(baseUri)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(dto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(dto.getEmail()), String.class));
    }

    @Test
    void notSaveBecauseEmailAlreadyExist() throws Exception {
        EmailAlreadyExistException e = new EmailAlreadyExistException("This email = " + dto.getEmail() + " already exists");
        when(userService.save(any()))
                .thenThrow(e);
        performMvcPost(baseUri)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is(e.getMessage())));
    }

    @Test
    void update() throws Exception {
        when(userService.update(any()))
                .thenReturn(dto);

        performMvcPatch(baseUri + idUri + dto.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(dto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(dto.getEmail()), String.class));
    }

    @Test
    void notUpdateBecauseEmailAlreadyExist() throws Exception {
        EmailAlreadyExistException e = new EmailAlreadyExistException("This email = " + dto.getEmail() + " already exists");
        when(userService.update(any()))
                .thenThrow(e);

        performMvcPatch(baseUri + idUri + dto.getId())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is(e.getMessage())));
    }

    @Test
    void getById() throws Exception {
        when(userService.get(dto.getId()))
                .thenReturn(dto);
        performMvcGet(baseUri + idUri + dto.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(dto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(dto.getEmail()), String.class));
    }

    @Test
    void getWithNotExistingId() throws Exception {
        when(userService.get(dto.getId()))
                .thenThrow(new NotFoundException("User not found with id: " + dto.getId()));
        performMvcGet(baseUri + idUri + dto.getId())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found with id: " + dto.getId())));
    }
}