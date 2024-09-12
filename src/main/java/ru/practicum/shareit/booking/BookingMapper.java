package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public BookingDtoCreate modelToDto(Booking model) {

        return null;
    }

    public Booking dtoToModel(BookingDtoCreate dto, ItemDto itemDto, UserDto bookerDto) {
        Booking model = new Booking();
        model.setStart(dto.getStart());
        model.setEnd(dto.getEnd());
        model.setItem(itemMapper.dtoToModel(itemDto));
        model.setBooker(userMapper.dtoToModel(bookerDto));
        model.setStatus(dto.getStatus());
        return model;
    }

    public BookingDto modelToDtoResponse(Booking model) {
        BookingDto dto = new BookingDto();
        dto.setId(model.getId());
        dto.setStart(model.getStart());
        dto.setEnd(model.getEnd());
        dto.setItem(itemMapper.modelToDto(model.getItem()));
        dto.setBooker(userMapper.modelToDto(model.getBooker()));
        dto.setStatus(model.getStatus());
        return dto;
    }
}
