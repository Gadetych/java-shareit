package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.model.BookingWithItemAndUser;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingWithItemRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto save(BookingDtoCreate dtoCreate) {
        ItemDto itemDto = itemService.get(dtoCreate.getBookerId(), dtoCreate.getItemId());
        if (!itemDto.getAvailable()) {
            throw new UnavailableItemException("Saving. Item is not available, id = " + itemDto.getId());
        }
        UserDto bookerDto = userService.get(dtoCreate.getBookerId());
        BookingWithItemAndUser booking = bookingMapper.dtoToModel(dtoCreate, itemDto, bookerDto);
        return bookingMapper.modelToDtoResponse(bookingRepository.save(booking));
    }

}
