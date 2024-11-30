package za.co.publiclibrary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.publiclibrary.controller.v1.IBookControllerV1;
import za.co.publiclibrary.dto.BookDTO;
import za.co.publiclibrary.service.BookService;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/28
 */

@RestController
@RequestMapping( "/api/book")
@RequiredArgsConstructor
@Slf4j
public class BookController implements IBookControllerV1
{

    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findBookById(@Parameter(description = "Book ID", required = true) @PathVariable Long id) {
        BookDTO bookDTO = bookService.findBookById(id);
        return ResponseEntity.ok(bookDTO);
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Parameter(description = "Create Book", required = true) @RequestBody @Validated BookDTO bookDTO) {
        BookDTO createdBook = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping
    public ResponseEntity<BookDTO> updateBook(@Parameter(description = "Update Book", required = true) @RequestBody @Validated BookDTO bookDTO) {
        BookDTO updatedBook = bookService.updateBook(bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @PatchMapping("/assign/{libraryId}/{bookId}")
    public ResponseEntity<BookDTO> assignBookToLibrary(@Parameter(description = "Library ID", required = true) @PathVariable Long libraryId,
                                                       @Parameter(description = "Book ID", required = true) @PathVariable Long bookId)
    {
        BookDTO assignedBook = bookService.assignBookToLibrary(libraryId, bookId);
        return ResponseEntity.ok(assignedBook);
    }

    @PatchMapping("/remove/{libraryId}/{bookId}")
    public ResponseEntity<Void> removeBookFromLibrary(@Parameter(description = "Library ID", required = true) @PathVariable Long libraryId,
                                                      @Parameter(description = "Book ID", required = true) @PathVariable Long bookId)
    {
        bookService.removeBookFromLibrary(libraryId, bookId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@Parameter(description = "Library ID", required = true) @PathVariable Long id)
    {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}