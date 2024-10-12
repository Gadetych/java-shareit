package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.AccessStatusItem;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest(properties = "spring.profiles.active=test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookingServiceImplTest {
    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingServiceImpl bookingServiceImpl;

    private BookingDto dto = new BookingDto();
    private BookingDtoCreate dtoCreate = new BookingDtoCreate();
    private long itemId;
    private LocalDateTime start = LocalDateTime.now();
    private LocalDateTime end = LocalDateTime.now().plusSeconds(5);
    private long bookerId;
    private AccessStatusItem status = AccessStatusItem.WAITING;

    private ItemDto itemDto = new ItemDto();
    private String itemName = "item";
    private String description = "description";
    private long ownerId;
    private boolean available = true;

    private UserDto userDto1 = new UserDto();
    private String userName1 = "user1";
    private String email1 = "user1@email.com";

    private UserDto userDto2 = new UserDto();
    private String userName2 = "user2";
    private String email2 = "user2@email.com";

    @BeforeEach
    void setUp() {
        userDto1.setName(userName1);
        userDto1.setEmail(email1);
        userDto1 = userService.save(userDto1);

        userDto2.setName(userName2);
        userDto2.setEmail(email2);
        userDto2 = userService.save(userDto2);
        bookerId = userDto2.getId();

        ownerId = userDto1.getId();
        itemDto.setName(itemName);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);
        itemDto = itemService.save(ownerId, itemDto);

        itemId = itemDto.getId();
        dtoCreate.setItemId(itemId);
        dtoCreate.setStart(start);
        dtoCreate.setEnd(end);
        dtoCreate.setBookerId(bookerId);
        dtoCreate.setStatus(status);
    }

    @Test
    void exists() {
    }

    @Test
    void testExists() {
    }

    @Test
    void create() {
        long id = bookingServiceImpl.create(dtoCreate).getId();
        BookingDto result = bookingServiceImpl.findById(id, bookerId);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getItem());
        assertEquals(itemId, result.getItem().getId());
        assertNotNull(result.getBooker());
        assertEquals(bookerId, result.getBooker().getId());
    }

    @Test
    void updateStatusBooking() {
        long id = bookingServiceImpl.create(dtoCreate).getId();
        bookingServiceImpl.updateStatusBooking(id, ownerId, true);
        em.clear();
        BookingDto result = bookingServiceImpl.findById(id, ownerId);

        assertEquals(AccessStatusItem.APPROVED, result.getStatus());
    }

    @Test
    void findById() {
        long id = bookingServiceImpl.create(dtoCreate).getId();
        BookingDto result = bookingServiceImpl.findById(id, bookerId);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getItem());
        assertEquals(itemId, result.getItem().getId());
        assertNotNull(result.getBooker());
        assertEquals(bookerId, result.getBooker().getId());
    }

    @Test
    void findAllByBooker() {
        bookingServiceImpl.create(dtoCreate);
        List<BookingDto> list = bookingServiceImpl.findAllByBooker(bookerId, BookingState.ALL);

        assertEquals(1, list.size());
    }

    @Test
    void findAllByBookerFromPast() throws InterruptedException {
        bookingServiceImpl.create(dtoCreate);
        TimeUnit.SECONDS.sleep(10);
        List<BookingDto> list = bookingServiceImpl.findAllByBooker(bookerId, BookingState.PAST);

        assertEquals(1, list.size());
    }

    @Test
    void findAllByBookerFromFuture() {
        dtoCreate.setStart(LocalDateTime.now().plusDays(1));
        dtoCreate.setEnd(LocalDateTime.now().plusDays(2));
        bookingServiceImpl.create(dtoCreate);
        List<BookingDto> list = bookingServiceImpl.findAllByBooker(bookerId, BookingState.FUTURE);

        assertEquals(1, list.size());
    }

    @Test
    void findAllByBookerFromWaiting() {
        bookingServiceImpl.create(dtoCreate);
        List<BookingDto> list = bookingServiceImpl.findAllByBooker(bookerId, BookingState.WAITING);

        assertEquals(1, list.size());
    }

    @Test
    void findAllByBookerFromRejected() {
        dtoCreate.setStatus(AccessStatusItem.REJECT);
        bookingServiceImpl.create(dtoCreate);
        List<BookingDto> list = bookingServiceImpl.findAllByBooker(bookerId, BookingState.REJECTED);

        assertEquals(1, list.size());
    }

    @Test
    void findAllByOwner() {
        bookingServiceImpl.create(dtoCreate);
        List<BookingDto> list = bookingServiceImpl.findAllByOwner(ownerId, BookingState.ALL);

        assertEquals(1, list.size());
    }

    @Test
    void findAllByOwnerFromPast() throws InterruptedException {
        bookingServiceImpl.create(dtoCreate);
        TimeUnit.SECONDS.sleep(10);
        List<BookingDto> list = bookingServiceImpl.findAllByOwner(ownerId, BookingState.PAST);

        assertEquals(1, list.size());
    }

    @Test
    void findAllByOwnerFromFuture() {
        dtoCreate.setStart(LocalDateTime.now().plusDays(1));
        dtoCreate.setEnd(LocalDateTime.now().plusDays(2));
        bookingServiceImpl.create(dtoCreate);
        List<BookingDto> list = bookingServiceImpl.findAllByOwner(ownerId, BookingState.FUTURE);

        assertEquals(1, list.size());
    }

    @Test
    void findAllByOwnerFromWaiting() {
        bookingServiceImpl.create(dtoCreate);
        List<BookingDto> list = bookingServiceImpl.findAllByOwner(ownerId, BookingState.WAITING);

        assertEquals(1, list.size());
    }

    @Test
    void findAllByOwnerFromRejected() {
        dtoCreate.setStatus(AccessStatusItem.REJECT);
        bookingServiceImpl.create(dtoCreate);
        List<BookingDto> list = bookingServiceImpl.findAllByOwner(ownerId, BookingState.REJECTED);

        assertEquals(1, list.size());
    }
}