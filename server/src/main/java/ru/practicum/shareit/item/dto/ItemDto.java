package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private Long requestId;
}
