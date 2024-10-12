package ru.practicum.shareit.exception;

public class UnavailableItemException extends NotValidException {
    public UnavailableItemException(String message) {
        super(message);
    }

    public UnavailableItemException(String message, Throwable cause) {
        super(message, cause);
    }
}
