package ru.practicum.shareit.exception;

public class BookingAccessException extends AccessException {
    public BookingAccessException(String message) {
        super(message);
    }

    public BookingAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
