package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.comment.dto.CommentDtoCreate;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDtoResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final String requestHeader = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestHeader(requestHeader) long ownerId,
                          @RequestBody ItemDto dto) {
        dto.setOwnerId(ownerId);
        log.info("Create item: {}, owner:{}", dto, ownerId);
        return itemService.save(ownerId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(requestHeader) long ownerId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto dto) {
        dto.setId(itemId);
        dto.setOwnerId(ownerId);
        log.info("Update item: {}, item id:{}, owner:{}", dto, itemId, ownerId);
        return itemService.update(ownerId, dto);
    }

    @GetMapping
    public List<ItemWithCommentsDtoResponse> findAllByOwnerId(@RequestHeader(requestHeader) long ownerId) {
        log.info("Find all items by ownerId: {}", ownerId);
        return itemService.findAllItemsByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(requestHeader) long ownerId,
                         @RequestParam("text") String text) {
        log.info("Find all items by ownerId: {}, text: {}", ownerId, text);
        return itemService.search(ownerId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse createComment(@RequestHeader(requestHeader) long authorId,
                                            @PathVariable long itemId,
                                            @RequestBody CommentDtoCreate dto) {
        log.info("Create comment: {} for item: {}, authorId:{}", dto, itemId, authorId);
        return itemService.createComment(dto, authorId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemWithCommentsDtoResponse getItemWithComments(@RequestHeader(requestHeader) long userId,
                                                           @PathVariable long itemId) {
        log.info("Find item with comments, itemId: {}, userId:{}", itemId, userId);
        return itemService.getItemWithComments(itemId, userId);
    }
}
