package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDtoCreate {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private long bookerId;
    private AccessStatusItem status = AccessStatusItem.WAITING;
}
