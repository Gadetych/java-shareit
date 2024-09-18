package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDtoResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

@UtilityClass
public class ItemMapper {
    public static ItemDto modelToDto(Item model) {
        ItemDto dto = new ItemDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setAvailable(model.getAvailable());
        dto.setOwnerId(model.getOwnerId());
        return dto;
    }

    public static Item dtoToModel(ItemDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwnerId(dto.getOwnerId());
        return item;
    }

    public static ItemWithCommentsDtoResponse modelToDtoWithComments(Item model, List<CommentDtoResponse> comments, Map<String, BookingDto> bookingDtoMap) {
        return ItemWithCommentsDtoResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .available(model.getAvailable())
                .ownerId(model.getOwnerId())
                .comments(comments)
                .lastBooking(bookingDtoMap.get("lastBooking"))
                .nextBooking(bookingDtoMap.get("nextBooking"))
                .build();
    }
}
