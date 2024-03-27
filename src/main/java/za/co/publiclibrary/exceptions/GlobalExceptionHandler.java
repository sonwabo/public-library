package za.co.publiclibrary.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/26
 * @TIME 15:44
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{
    @ExceptionHandler(LibraryNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<CustomError> handleLibraryNotFoundEx(final LibraryNotFoundException ex)
    {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(NOT_FOUND)
                .body(new CustomError(NOT_FOUND.value(), "Library not found for provided id"));
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<CustomError> handleBookNotFoundEx(final BookNotFoundException ex)
    {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(NOT_FOUND)
                .body(new CustomError(NOT_FOUND.value(), "Book not found for provided id"));
    }

    record CustomError(int errorCode, String msg){}
}

