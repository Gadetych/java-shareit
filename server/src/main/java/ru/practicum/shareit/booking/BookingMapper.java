package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

@UtilityClass
public class BookingMapper {

    public static Booking dtoToModel(BookingDtoCreate dto, ItemDto itemDto, UserDto bookerDto) {
        Booking model = new Booking();
        model.setStart(dto.getStart());
        model.setEnd(dto.getEnd());
        model.setItem(ItemMapper.dtoToModel(itemDto));
        model.setBooker(UserMapper.dtoToModel(bookerDto));
        model.setStatus(dto.getStatus());
        return model;
    }

    public static BookingDto modelToDtoResponse(Booking model) {
        BookingDto dto = new BookingDto();
        dto.setId(model.getId());
        dto.setStart(model.getStart());
        dto.setEnd(model.getEnd());
        dto.setItem(ItemMapper.modelToDto(model.getItem()));
        dto.setBooker(UserMapper.modelToDto(model.getBooker()));
        dto.setStatus(model.getStatus());
        return dto;
    }
}
