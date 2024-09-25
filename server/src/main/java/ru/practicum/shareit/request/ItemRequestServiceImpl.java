package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto) {
        log.debug("Creating. Item request dto: \n{}", itemRequestDto);
        ItemRequest itemRequest = ItemRequestMapper.dtoToModel(itemRequestDto);
        ItemRequestDto result = ItemRequestMapper.modelToDto(itemRequestRepository.save(itemRequest));
        log.debug("Creating. Item request model: \n{}", result);
        return result;
    }

    @Override
    public List<ItemRequestDto> findAllByRequestor(long requestorId) {
        log.debug("Finding all item requests by requestor. Requestor: \n{}", requestorId);
        List<ItemRequest> listItemRequests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId);
        log.debug("Finding all item requests by requestor. List with model: \n{}", listItemRequests);
        List<ItemRequestDto> result = listItemRequests.stream()
                .map(ItemRequestMapper::modelToDto)
                .toList();
        log.debug("Finding all item requests by requestor. List with dto: \n{}", result);
        return result;
    }

    @Override
    public List<ItemRequestDto> findAll() {
        log.debug("Finding all item requests.");
        List<ItemRequest> listItemRequests = itemRequestRepository.findAllOrderByCreatedDesc();
        log.debug("Finding all item requests. List with model: \n{}", listItemRequests);
        List<ItemRequestDto> result = listItemRequests.stream()
                .map(ItemRequestMapper::modelToDto)
                .toList();
        log.debug("Finding all item requests. List with dto: \n{}", result);
        return result;
    }

    @Override
    public ItemRequestDto findById(long requestId) {
        log.debug("Finding item request by id: {}", requestId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Not found Item request by id: " + requestId));
        log.debug("Finding item request model: \n{}", itemRequest);
        ItemRequestDto result = ItemRequestMapper.modelToDto(itemRequest);
        log.debug("Finding item request dto: \n{}", result);
        return result;
    }
}
