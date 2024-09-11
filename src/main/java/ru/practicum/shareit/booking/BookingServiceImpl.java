package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;
    private final ItemMapper itemMapper;

    @Override
    public BookingDto create(BookingDtoCreate dtoCreate) {
        Booking booking = bookingMapper.dtoToModel(dtoCreate);
        ItemDto itemDto = itemService.get(dtoCreate.getItemId(), dtoCreate.getBookerId());
        Item item = itemMapper.dtoToModel(itemDto);
        booking.setItem(item);
        return bookingMapper.modelToDtoResponse(bookingRepository.save(booking));
    }
}
