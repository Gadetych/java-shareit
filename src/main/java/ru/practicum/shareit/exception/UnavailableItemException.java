package ru.practicum.shareit.exception;

public class UnavailableItemException extends RuntimeException {
    public UnavailableItemException(String message) {
        super(message);
    }

    public UnavailableItemException(String message, Throwable cause) {
        super(message, cause);
    }
}
