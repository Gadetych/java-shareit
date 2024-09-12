package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.AccessStatusItem;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BookingAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;

    @Override
    public void exists(long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException("Booking with id " + bookingId + " not found");
        }
    }

    @Override
    public void exists(long bookingId, long ownerId) {
        if (!bookingRepository.existsByOwnerId(bookingId, ownerId)) {
            throw new BookingAccessException("User with id " + ownerId + " not owner of booking with id " + bookingId);
        }
    }

    @Override
    public BookingDto save(BookingDtoCreate dtoCreate) {
        ItemDto itemDto = itemService.get(dtoCreate.getBookerId(), dtoCreate.getItemId());
        if (!itemDto.getAvailable()) {
            throw new UnavailableItemException("Saving. Item is not available, id = " + itemDto.getId());
        }
        UserDto bookerDto = userService.get(dtoCreate.getBookerId());
        Booking booking = bookingMapper.dtoToModel(dtoCreate, itemDto, bookerDto);
        return bookingMapper.modelToDtoResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingDto updateStatusBooking(long bookingId, long ownerId, boolean approved) {
        exists(bookingId, ownerId);
        userService.exists(ownerId);
        AccessStatusItem newStatus;
        if (approved) {
            newStatus = AccessStatusItem.APPROVED;
        } else {
            newStatus = AccessStatusItem.REJECT;
        }
        bookingRepository.updateBooking(bookingId, ownerId, newStatus);
        return bookingMapper.modelToDtoResponse(bookingRepository.findById(bookingId).get());
    }

    @Override
    public BookingDto get(long bookingId, long userId) {
        exists(bookingId);
        Booking result = bookingRepository.findByBookingId(bookingId, userId).orElseThrow(() -> new BookingAccessException("Denied access for user id = " + userId));
        return bookingMapper.modelToDtoResponse(result);
    }

    @Override
    public List<BookingDto> getAllByBooker(long bookerId, BookingState state) {
        List<Booking> modelList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL -> modelList = bookingRepository.findAllByBookerId(bookerId);
            case PAST -> modelList = bookingRepository.findAllByBookerIdAndEndBefore(bookerId, now);
            case FUTURE -> modelList = bookingRepository.findAllByBookerIdAndStartAfter(bookerId, now);
            case WAITING ->
                    modelList = bookingRepository.findAllByBookerIdAndStatus(bookerId, AccessStatusItem.WAITING);
            case REJECTED ->
                    modelList = bookingRepository.findAllByBookerIdAndStatus(bookerId, AccessStatusItem.REJECT);
            case CURRENT -> modelList = bookingRepository.findAllByBookerIdAndNowBetween(bookerId, now);
        }
        return modelList.stream()
                .map(bookingMapper::modelToDtoResponse)
                .toList();
    }

}
