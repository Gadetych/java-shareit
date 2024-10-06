package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@DisabledInAotMode
class ItemControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    private final String requestHeader = "X-Sharer-User-Id";
    ItemDto itemDto1 = new ItemDto();
    ItemDto itemDto2 = new ItemDto();
    String itemName1 = "item1";
    String itemName2 = "item2";
    String itemDesc1 = "desc1";
    String itemDesc2 = "desc2";
    String baseUri = "/items";
    String searchUri = "/search?text=";
    long itemId1 = 1L;
    long userId = 1L;
    String searchText = "text";


    @BeforeEach
    void setUp() {
        itemDto1.setName(itemName1);
        itemDto1.setDescription(itemDesc1);
        itemDto1.setAvailable(true);
    }

    ResultActions performMvc(MockHttpServletRequestBuilder builder) throws Exception {
        return mvc.perform(builder);
    }

    MockHttpServletRequestBuilder setRequestHeaders(MockHttpServletRequestBuilder builder) throws JsonProcessingException {
        return builder
                .content(mapper.writeValueAsString(itemDto1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .header(requestHeader, userId);
    }

    MockHttpServletRequestBuilder setRequestHeadersWithoutBody(MockHttpServletRequestBuilder builder) throws JsonProcessingException {
        return builder
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .header(requestHeader, userId);
    }

    @Test
    void createItemWithNonExistentUser() throws Exception {
        when(itemService.save(anyLong(), any(ItemDto.class)))
                .thenThrow(NotFoundException.class);

        performMvc(setRequestHeaders(post(baseUri)))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        performMvc(setRequestHeaders(post(baseUri)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateItemAvailableField() throws Exception {
        itemDto2.setAvailable(true);

        performMvc(setRequestHeaders(patch(baseUri + "/" + itemId1)))
                .andExpect(status().isOk());
    }

    @Test
    void updateItemWithNameField() throws Exception {
        itemDto2.setName(itemName2);

        performMvc(setRequestHeaders(patch(baseUri + "/" + itemId1)))
                .andExpect(status().isOk());
    }

    @Test
    void updateDescriptionField() throws Exception {
        itemDto2.setDescription(itemDesc2);

        performMvc(setRequestHeaders(patch(baseUri + "/" + itemId1)))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        performMvc(setRequestHeaders(patch(baseUri + "/" + itemId1)))
                .andExpect(status().isOk());
    }

    @Test
    void findItemWithComments() throws Exception {
        performMvc(setRequestHeadersWithoutBody(get(baseUri + "/" + itemId1)))
                .andExpect(status().isOk());
    }

    @Test
    void findAllByOwnerId() throws Exception {
        performMvc(setRequestHeadersWithoutBody(get(baseUri)))
                .andExpect(status().isOk());
    }

    @Test
    void search() throws Exception {
        performMvc(setRequestHeadersWithoutBody(get(baseUri + searchUri + searchText)))
                .andExpect(status().isOk());
    }

    @Test
    void createComment() throws Exception {
        CommentDtoCreate commentDtoCreate = new CommentDtoCreate();
        commentDtoCreate.setText("text");

        mvc.perform(post(baseUri + "/" + itemId1 + "/comment")
                        .content(mapper.writeValueAsString(commentDtoCreate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(requestHeader, userId))
                .andExpect(status().isOk());
    }

}