package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.booking.dto.AccessStatusItem;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;

@Component
@RequiredArgsConstructor
public class BookingMapper implements Mapper<Booking, BookingDtoCreate> {
    private final ItemMapper itemMapper;

    @Override
    public BookingDtoCreate modelToDto(Booking model) {

        return null;
    }

    @Override
    public Booking dtoToModel(BookingDtoCreate dto) {
        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        Item item = new Item();
        item.setId(dto.getItemId());
        booking.setItem(item);
        booking.setBookerId(dto.getBookerId());
        booking.setStatus(AccessStatusItem.WAITING);
        return booking;
    }

    public BookingDto modelToDtoResponse(Booking model) {
        BookingDto dto = new BookingDto();
        dto.setId(model.getId());
        dto.setStart(model.getStart());
        dto.setEnd(model.getEnd());
        dto.setItem(itemMapper.modelToDto(model.getItem()));
        dto.setBookerId(model.getBookerId());
        dto.setStatus(model.getStatus());
        return dto;
    }
}
