package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.item.dto.ItemFeedbackDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    @Null(groups = Marker.Create.class)
    private Long id;
    private long requestorId;
    private String description;
    private LocalDateTime created;
    @Null(groups = Marker.Create.class)
    private List<ItemFeedbackDto> items;
}
