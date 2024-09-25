package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final String requestHeader = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Validated(Marker.Create.class)
    public ItemRequestDto create(@RequestHeader(name = requestHeader)
                                 @Positive long requestorId,
                                 @Valid
                                 @RequestBody ItemRequestDto itemRequestDto) {
        itemRequestDto.setRequestorId(requestorId);
        itemRequestDto.setCreated(LocalDateTime.now());
        return itemRequestService.create(itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> findAllByRequestor(@RequestHeader(name = requestHeader) @Positive long requestorId) {
        return itemRequestService.findAllByRequestor(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll() {
        return itemRequestService.findAll();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@PathVariable("requestId") @Positive long requestId) {
        return itemRequestService.findById(requestId);
    }
}
