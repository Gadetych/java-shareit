package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.item.comment.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.Marker;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemClient itemClient;
    private final String requestHeader = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.Create.class)
    public ResponseEntity<Object> create(@RequestHeader(requestHeader)
                                         @Positive long ownerId,
                                         @RequestBody
                                         @Valid ItemDto dto) {
        log.info("Create item: {}, owner:{}", dto, ownerId);
        return itemClient.create(ownerId, dto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(requestHeader)
                                         @Positive long ownerId,
                                         @PathVariable
                                         @Positive long itemId,
                                         @RequestBody
                                         @Valid ItemDto dto) {
        log.info("Update item: {}, item id:{}, owner:{}", dto, itemId, ownerId);
        return itemClient.update(ownerId, itemId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByOwnerId(@RequestHeader(requestHeader)
                                                   @Positive long ownerId) {
        log.info("Find all items by ownerId: {}", ownerId);
        return itemClient.findAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(requestHeader)
                                         @Positive long ownerId,
                                         @RequestParam("text") String text) {
        log.info("Find all items by ownerId: {}, text: {}", ownerId, text);
        return itemClient.search(ownerId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(requestHeader)
                                                @Positive long authorId,
                                                @PathVariable long itemId,
                                                @RequestBody
                                                @Valid CommentDtoCreate dto) {
        log.info("Create comment: {} for item: {}, authorId:{}", dto, itemId, authorId);
        return itemClient.createComment(authorId, itemId, dto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemWithComments(@RequestHeader(requestHeader)
                                                       @Positive
                                                       long userId,
                                                       @PathVariable
                                                       @Positive
                                                       long itemId) {
        log.info("Find item with comments, itemId: {}, userId:{}", itemId, userId);
        return itemClient.findItemWithComments(itemId, userId);
    }
}
