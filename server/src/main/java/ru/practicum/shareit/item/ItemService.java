package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.dto.CommentDtoCreate;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDtoResponse;

import java.util.List;

public interface ItemService {
    boolean exists(Long itemId);

    ItemDto save(Long ownerId, ItemDto dto);

    ItemDto update(Long ownerId, ItemDto dto);

    ItemDto get(Long ownerId, Long id);

    List<ItemWithCommentsDtoResponse> findAllItemsByOwnerId(Long ownerId);

    List<ItemDto> search(Long ownerId, String text);

    CommentDtoResponse createComment(CommentDtoCreate dto, long authorId, long itemId);

    ItemWithCommentsDtoResponse getItemWithComments(long itemId, long userId);
}
