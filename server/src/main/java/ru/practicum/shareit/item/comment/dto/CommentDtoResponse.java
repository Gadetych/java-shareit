package ru.practicum.shareit.item.comment.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class CommentDtoResponse {
    private long id;
    private String text;
    private UserDto author;
    private ItemDto item;
    private LocalDateTime created;
    private String authorName;
}
