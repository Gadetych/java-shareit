package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemFeedbackDto {
    private long id;
    private long ownerId;
    private String name;
}
