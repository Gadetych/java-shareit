package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final String requestHeader = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader(name = requestHeader) long requestorId,
                                 @RequestBody ItemRequestDto itemRequestDto) {
        itemRequestDto.setRequestorId(requestorId);
        log.info("Creating request: {}", itemRequestDto);
        return itemRequestService.create(itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> findAllByRequestor(@RequestHeader(name = requestHeader) long requestorId) {
        log.info("Finding all requests by requestorId: {}", requestorId);
        return itemRequestService.findAllByRequestor(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll() {
        log.info("Finding all requests");
        return itemRequestService.findAll();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@PathVariable("requestId") long requestId) {
        log.info("Finding request by id: {}", requestId);
        return itemRequestService.findById(requestId);
    }
}
