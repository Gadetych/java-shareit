package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @InjectMocks
    private ItemRequestServiceImpl service;
    @Mock
    private ItemRequestRepository repository;
    private Long id = 1L;
    private long requestorId = 1L;
    private String description = "description";
    private LocalDateTime created = LocalDateTime.now();
    private ItemRequestDto dto = ItemRequestDto.builder()
            .requestorId(requestorId)
            .description(description)
            .created(created)
            .items(List.of())
            .build();
    private ItemRequest model = new ItemRequest();

    @BeforeEach
    void setUp() {
        model.setId(id);
        model.setRequestorId(requestorId);
        model.setDescription(description);
        model.setCreated(created);
        model.setItems(List.of());
    }

    @Test
    void create() {
        when(repository.save(any())).thenReturn(model);
        ItemRequestDto result = service.create(dto);
        dto.setId(id);

        assertEquals(dto, result);
    }

    @Test
    void findAllByRequestor() {
        when(repository.findAllByRequestorIdOrderByCreatedDesc(requestorId)).thenReturn(List.of(model));
        List<ItemRequestDto> result = service.findAllByRequestor(requestorId);
        dto.setId(id);

        assertEquals(List.of(dto), result);
    }

    @Test
    void findAll() {
        when(repository.findAllOrderByCreatedDesc()).thenReturn(List.of(model));
        List<ItemRequestDto> result = service.findAll();
        dto.setId(id);

        assertEquals(List.of(dto), result);
    }

    @Test
    void findById() {
        when(repository.findById(id)).thenReturn(Optional.of(model));
        ItemRequestDto result = service.findById(id);
        dto.setId(id);

        assertEquals(dto, result);
    }
}