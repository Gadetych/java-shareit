package ru.practicum.shareit.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    @MockBean
    private final ItemRequestServiceImpl service;
    private final ItemRequestController controller;
    private final String requestHeader = "X-Sharer-User-Id";
    String baseUri = "/requests";
    private Long id = 1L;
    private long requestorId = 1L;
    private String description = "description";
    private LocalDateTime created = LocalDateTime.now();
    private ItemRequestDto requestDto1 = ItemRequestDto.builder()
            .requestorId(requestorId)
            .description(description)
            .created(created)
            .items(List.of())
            .build();
    private ItemRequest request1 = new ItemRequest();

    @BeforeEach
    void setUp() {
        request1.setId(id);
        request1.setRequestorId(requestorId);
        request1.setDescription(description);
        request1.setCreated(created);
        request1.setItems(List.of());
    }

    ResultActions performMvc(MockHttpServletRequestBuilder builder) throws Exception {
        return mvc.perform(builder);
    }

    MockHttpServletRequestBuilder setRequestHeaders(MockHttpServletRequestBuilder builder) throws JsonProcessingException {
        return builder
                .content(mapper.writeValueAsString(requestDto1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .header(requestHeader, requestorId);
    }

    MockHttpServletRequestBuilder setRequestHeadersWithoutBody(MockHttpServletRequestBuilder builder) throws JsonProcessingException {
        return builder
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .header(requestHeader, requestorId);
    }

    @Test
    void create() throws Exception {
        performMvc(setRequestHeaders(post(baseUri))).andExpect(status().isCreated());
    }

    @Test
    void findAllByRequestor() throws Exception {
        performMvc(setRequestHeadersWithoutBody(get(baseUri + "/" + requestorId))).andExpect(status().isOk());
    }

    @Test
    void findAll() throws Exception {
        performMvc(setRequestHeadersWithoutBody(get(baseUri))).andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        performMvc(setRequestHeadersWithoutBody(get(baseUri + "/" + requestorId))).andExpect(status().isOk());
    }
}