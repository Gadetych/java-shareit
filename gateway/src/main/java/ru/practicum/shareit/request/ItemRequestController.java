package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.Marker;

@Controller
@RequiredArgsConstructor
@RequestMapping("/requests")
@Validated
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private final String requestHeader = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Validated(Marker.Create.class)
    public ResponseEntity<Object> create(@RequestHeader(name = requestHeader)
                                         @Positive long requestorId,
                                         @Valid
                                         @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Create request: {}", itemRequestDto);
        return itemRequestClient.create(requestorId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByRequestor(@RequestHeader(name = requestHeader) @Positive long requestorId) {
        log.info("Find all requests by requestor: {}", requestorId);
        return itemRequestClient.findAllByRequestor(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll() {
        log.info("Find all requests");
        return itemRequestClient.findAll();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@PathVariable("requestId") @Positive long requestId) {
        log.info("Find request by id: {}", requestId);
        return itemRequestClient.findById(requestId);
    }
}
