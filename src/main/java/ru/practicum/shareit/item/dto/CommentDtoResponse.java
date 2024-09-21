package ru.practicum.shareit.item.dto;

import lombok.Data;
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
