package za.co.publiclibrary.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static java.util.List.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/26
 * @TIME 15:44
 */

@RestControllerAdvice
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
                .body(new CustomError(NOT_FOUND.value(), of("Library not found for provided id")));
    }


    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<CustomError> handleBookNotFoundEx(final BookNotFoundException ex)
    {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(NOT_FOUND)
                .body(new CustomError(NOT_FOUND.value(), of("Book not found for provided id")));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> handleValidationExceptions(MethodArgumentNotValidException ex)
    {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity
                .badRequest()
                .body(new CustomError(BAD_REQUEST.value(), errors));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CustomError> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex)
    {
        log.info(ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new CustomError(CLIENT_ERROR.value(), of("Library name selected already exists")));
    }

    record CustomError(int code, List<String> message) implements Serializable {}
}