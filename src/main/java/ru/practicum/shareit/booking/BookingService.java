package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;

public interface BookingService {

    BookingDto save(BookingDtoCreate dtoCreate);
}
