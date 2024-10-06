package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findAllByRequestor(long requestorId);

    List<ItemRequestDto> findAll();

    ItemRequestDto findById(long requestId);
}
