package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookingControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    @MockBean
    private final BookingClient client;
    private final String requestHeader = "X-Sharer-User-Id";
    private BookItemRequestDto dto = new BookItemRequestDto(1, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
    private long userId = 1;
    private long bookingId = 1;
    private String baseUri = "/bookings";

    ResultActions performMvc(MockHttpServletRequestBuilder builder) throws Exception {
        return mvc.perform(builder);
    }

    MockHttpServletRequestBuilder setRequestHeaders(MockHttpServletRequestBuilder builder) throws JsonProcessingException {
        return builder
                .content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .header(requestHeader, userId);
    }

    @Test
    void createFailedByStartInPast() throws Exception {
        dto.setStart(LocalDateTime.now().minusHours(1));
        performMvc(setRequestHeaders(post(baseUri))).andExpect(status().isBadRequest());
    }

    @Test
    void createFailedByEndInPast() throws Exception {
        dto.setEnd(LocalDateTime.now().minusHours(1));
        performMvc(setRequestHeaders(post(baseUri))).andExpect(status().isBadRequest());
    }

    @Test
    void createFailedByStartEqualEnd() throws Exception {
        dto.setStart(LocalDateTime.now().minusHours(1));
        dto.setEnd(LocalDateTime.now().minusHours(1));
        performMvc(setRequestHeaders(post(baseUri))).andExpect(status().isBadRequest());
    }

    @Test
    void createFailedByStartEqualNull() throws Exception {
        dto.setStart(null);
        performMvc(setRequestHeaders(post(baseUri))).andExpect(status().isBadRequest());
    }

    @Test
    void createFailedByEndEqualNull() throws Exception {
        dto.setEnd(null);
        performMvc(setRequestHeaders(post(baseUri))).andExpect(status().isBadRequest());
    }

    @Test
    void create() throws Exception {
        performMvc(setRequestHeaders(post(baseUri))).andExpect(status().isCreated());
    }

    @Test
    void updateStatusBooking() throws Exception {
        performMvc(setRequestHeaders(patch(baseUri + "/" + bookingId + "?approved=true"))).andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        performMvc(setRequestHeaders(get(baseUri + "/" + bookingId))).andExpect(status().isOk());
    }

    @Test
    void findAllByBooker() throws Exception {
        performMvc(setRequestHeaders(get(baseUri))).andExpect(status().isOk());
    }

    @Test
    void findAllByOwner() throws Exception {
        performMvc(setRequestHeaders(get(baseUri + "/owner"))).andExpect(status().isOk());
    }
}