package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.comment.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> create(Long ownerId, ItemDto dto) {
        return post("", ownerId, dto);
    }

    public ResponseEntity<Object> update(long ownerId, long itemId, ItemDto dto) {
        return patch("/" + itemId, ownerId, dto);
    }

    public ResponseEntity<Object> findAllByOwnerId(long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> search(Long ownerId, String text) {
        Map<String, Object> params = new HashMap<>();
        params.put("text", text);
        return get("/search?text={text}", ownerId, params);
    }

    public ResponseEntity<Object> createComment(long authorId, long itemId, CommentDtoCreate dto) {
        return post("/" + itemId + "/comment", authorId, dto);
    }

    public ResponseEntity<Object> findItemWithComments(long itemId, long userId) {
        return get("/" + itemId, userId);
    }


}
