package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDtoCreate {
    private String text;
    private LocalDateTime created = LocalDateTime.now();
}
