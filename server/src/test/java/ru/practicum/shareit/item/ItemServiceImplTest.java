package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private ItemServiceImpl itemService;
    private ItemDto itemDto = new ItemDto();
    private Item itemModel = new Item();

    @BeforeEach
    void setUp() {
        itemDto.setId(1L);
        itemDto.setName("test");
        itemDto.setDescription("description");
        itemDto.setOwnerId(1L);
        itemDto.setAvailable(true);

        itemModel.setId(1L);
        itemModel.setName("test");
        itemModel.setDescription("description");
        itemModel.setOwnerId(1L);
        itemModel.setAvailable(true);
    }

    @Test
    void save() {
        itemDto.setName("test");
        itemDto.setDescription("description");
        itemDto.setOwnerId(1L);
        itemDto.setAvailable(true);

        itemModel.setName("test");
        itemModel.setDescription("description");
        itemModel.setOwnerId(1L);
        itemModel.setAvailable(true);

        Item result = new Item();
        result.setId(1L);
        result.setName("test");
        result.setDescription("description");
        result.setOwnerId(1L);
        result.setAvailable(true);

        when(itemRepository.save(itemModel))
                .thenReturn(result);
        itemService.save(itemDto.getOwnerId(), itemDto);

        verify(itemRepository, times(1)).save(itemModel);
    }

    @Test
    void updateAllItemField() {
        itemDto.setId(1L);
        itemDto.setName("new name");
        itemDto.setDescription("new description");
        itemDto.setOwnerId(1L);
        itemDto.setAvailable(true);

        itemModel.setId(1L);
        itemModel.setName("name");
        itemModel.setDescription("description");
        itemModel.setOwnerId(1L);
        itemModel.setAvailable(false);

        Item result = new Item();
        result.setId(1L);
        result.setName("new name");
        result.setDescription("new description");
        result.setOwnerId(1L);
        result.setAvailable(true);

        when(itemRepository.existsItemForUser(itemDto.getOwnerId(), itemDto.getId()))
                .thenReturn(true);
        when(itemRepository.existsById(itemDto.getId()))
                .thenReturn(true);
        when(itemRepository.findById(itemDto.getId()))
                .thenReturn(Optional.of(itemModel));
        when(itemRepository.save(result))
                .thenReturn(result);
        itemService.update(itemDto.getOwnerId(), itemDto);

        verify(itemRepository, times(1))
                .save(result);
    }

    @Test
    void updateNameField() {
        itemDto.setId(1L);
        itemDto.setName("new name");
        itemDto.setDescription(null);
        itemDto.setOwnerId(1L);
        itemDto.setAvailable(null);

        itemModel.setId(1L);
        itemModel.setName("name");
        itemModel.setDescription("description");
        itemModel.setOwnerId(1L);
        itemModel.setAvailable(false);

        Item result = new Item();
        result.setId(1L);
        result.setName("new name");
        result.setDescription("description");
        result.setOwnerId(1L);
        result.setAvailable(false);

        when(itemRepository.existsItemForUser(itemDto.getOwnerId(), itemDto.getId()))
                .thenReturn(true);
        when(itemRepository.existsById(itemDto.getId()))
                .thenReturn(true);
        when(itemRepository.findById(itemDto.getId()))
                .thenReturn(Optional.of(itemModel));
        when(itemRepository.save(result))
                .thenReturn(result);
        itemService.update(itemDto.getOwnerId(), itemDto);

        verify(itemRepository, times(1))
                .save(result);
    }

    @Test
    void updateDescriptionField() {
        itemDto.setId(1L);
        itemDto.setName(null);
        itemDto.setDescription("new description");
        itemDto.setOwnerId(1L);
        itemDto.setAvailable(null);

        itemModel.setId(1L);
        itemModel.setName("name");
        itemModel.setDescription("description");
        itemModel.setOwnerId(1L);
        itemModel.setAvailable(false);

        Item result = new Item();
        result.setId(1L);
        result.setName("name");
        result.setDescription("new description");
        result.setOwnerId(1L);
        result.setAvailable(false);

        when(itemRepository.existsItemForUser(itemDto.getOwnerId(), itemDto.getId()))
                .thenReturn(true);
        when(itemRepository.existsById(itemDto.getId()))
                .thenReturn(true);
        when(itemRepository.findById(itemDto.getId()))
                .thenReturn(Optional.of(itemModel));
        when(itemRepository.save(result))
                .thenReturn(result);
        itemService.update(itemDto.getOwnerId(), itemDto);

        verify(itemRepository, times(1))
                .save(result);
    }

    @Test
    void updateAvailableField() {
        itemDto.setId(1L);
        itemDto.setName(null);
        itemDto.setDescription(null);
        itemDto.setOwnerId(1L);
        itemDto.setAvailable(true);

        itemModel.setId(1L);
        itemModel.setName("name");
        itemModel.setDescription("description");
        itemModel.setOwnerId(1L);
        itemModel.setAvailable(false);

        Item result = new Item();
        result.setId(1L);
        result.setName("name");
        result.setDescription("description");
        result.setOwnerId(1L);
        result.setAvailable(true);

        when(itemRepository.existsItemForUser(itemDto.getOwnerId(), itemDto.getId()))
                .thenReturn(true);
        when(itemRepository.existsById(itemDto.getId()))
                .thenReturn(true);
        when(itemRepository.findById(itemDto.getId()))
                .thenReturn(Optional.of(itemModel));
        when(itemRepository.save(result))
                .thenReturn(result);
        itemService.update(itemDto.getOwnerId(), itemDto);

        verify(itemRepository, times(1))
                .save(result);
    }

    @Test
    void get() {
        itemModel.setId(1L);
        itemModel.setName("test");
        itemModel.setDescription("description");
        itemModel.setOwnerId(1L);
        itemModel.setAvailable(true);

        User booker = new User(1L, "test", "test");

        LocalDateTime now = LocalDateTime.now();
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(now.plusHours(1));
        booking1.setEnd(now.plusHours(2));
        booking1.setItem(itemModel);
        booking1.setBooker(booker);
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(now.plusHours(3));
        booking2.setEnd(now.plusHours(4));
        booking2.setItem(itemModel);
        booking2.setBooker(booker);
        Booking booking3 = new Booking();
        booking3.setId(3L);
        booking3.setStart(now.plusHours(5));
        booking3.setEnd(now.plusHours(6));
        booking3.setItem(itemModel);
        booking3.setBooker(booker);

        List<Booking> list = List.of(booking3, booking2, booking1);
        when(itemRepository.existsById(itemModel.getId()))
                .thenReturn(true);
        when(itemRepository.findById(itemModel.getId()))
                .thenReturn(Optional.of(itemModel));
        when(bookingRepository.findAllByItemIdAndEndBeforeOrderByEndDesc(anyLong(), any()))
                .thenReturn(list);

        ItemDto result = itemService.get(itemModel.getOwnerId(), itemModel.getId());
        assertEquals(result.getNextBooking(), BookingMapper.modelToDtoResponse(booking1));
        assertEquals(result.getLastBooking(), BookingMapper.modelToDtoResponse(booking3));
    }

    @Test
    void searchTextIsBlank() {
        Long ownerId = 1L;
        String text = null;
        List<ItemDto> result = itemService.search(ownerId, text);
        assertTrue(result.isEmpty());

        text = "";
        result = itemService.search(ownerId, text);
        assertTrue(result.isEmpty());
    }

    @Test
    void createComment() {
        String textComment = "comment";
        Long authorId = 1L;
        Long itemId = 1L;
        LocalDateTime now = LocalDateTime.now();
        CommentDtoCreate commentDtoCreate = new CommentDtoCreate();
        commentDtoCreate.setText(textComment);
        commentDtoCreate.setCreated(now.plusHours(4));

        User booker = new User(1L, "test", "test");

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(now.plusHours(1));
        booking1.setEnd(now.plusHours(2));
        booking1.setItem(itemModel);
        booking1.setBooker(booker);
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(now.plusHours(3));
        booking2.setEnd(now.plusHours(4));
        booking2.setItem(itemModel);
        booking2.setBooker(booker);
        Booking booking3 = new Booking();
        booking3.setId(3L);
        booking3.setStart(now.plusHours(5));
        booking3.setEnd(now.plusHours(6));
        booking3.setItem(itemModel);
        booking3.setBooker(booker);

        List<Booking> list = List.of(booking1, booking2, booking3);

        Comment commentModel = CommentMapper.dtoToModel(commentDtoCreate);
        commentModel.setId(1L);
        commentModel.setAuthor(booker);
        commentModel.setItem(itemModel);


        when(bookingRepository.findByBookerIdAndItemIdOrderByStart(authorId, itemId))
                .thenReturn(list);
        when(userService.get(authorId))
                .thenReturn(UserMapper.modelToDto(booker));
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(itemModel));
        when(commentRepository.save(any()))
                .thenReturn(commentModel);

        itemService.createComment(commentDtoCreate, authorId, itemId);

        verify(commentRepository, times(1))
                .save(any());
    }

    @Test
    void commentFutureBooking() {
        String textComment = "comment";
        Long authorId = 1L;
        Long itemId = 1L;
        LocalDateTime now = LocalDateTime.now();
        CommentDtoCreate commentDtoCreate = new CommentDtoCreate();
        commentDtoCreate.setText(textComment);
        commentDtoCreate.setCreated(now);

        User booker = new User(1L, "test", "test");

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(now.plusHours(1));
        booking1.setEnd(now.plusHours(2));
        booking1.setItem(itemModel);
        booking1.setBooker(booker);
        List<Booking> list = List.of(booking1);

        when(bookingRepository.findByBookerIdAndItemIdOrderByStart(authorId, itemId))
                .thenReturn(list);

        assertThrows(NotValidException.class, () -> itemService.createComment(commentDtoCreate, authorId, itemId));
    }

    @Test
    void findAllItemsByOwnerId() {
        List<Item> items = List.of(itemModel);
        when(itemRepository.findAllItemsByOwnerId(anyLong())).thenReturn(items);

        User booker = new User(1L, "test", "test");

        LocalDateTime now = LocalDateTime.now();
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(now.plusHours(1));
        booking1.setEnd(now.plusHours(2));
        booking1.setItem(itemModel);
        booking1.setBooker(booker);
        List<Booking> bookings = List.of(booking1);
        when(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(anyLong(), any())).thenReturn(bookings);

        String textComment = "comment";
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText(textComment);
        comment.setAuthor(booker);
        comment.setItem(itemModel);
        comment.setCreated(now);
        List<Comment> comments = List.of(comment);
        when(commentRepository.findAllByItemOwnerId(anyLong())).thenReturn(comments);

        List<ItemWithCommentsDtoResponse> result = itemService.findAllItemsByOwnerId(itemDto.getOwnerId());

        assertEquals(1, result.size());
        assertNotNull(result.getFirst().getComments());

    }

    @Test
    void getItemWithComments() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemModel));
        User user = new User(1L, "test", "test");
        String textComment = "comment";
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText(textComment);
        comment.setAuthor(user);
        comment.setItem(itemModel);
        comment.setCreated(LocalDateTime.now());
        List<Comment> comments = List.of(comment);
        when(commentRepository.findAllByItemIdOrderById(anyLong())).thenReturn(comments);

        ItemWithCommentsDtoResponse result = itemService.getItemWithComments(itemModel.getId(), itemModel.getOwnerId());

        assertNotNull(result);
        assertNotNull(result.getComments());
    }
}