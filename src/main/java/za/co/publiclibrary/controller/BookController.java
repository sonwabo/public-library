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
public class BookController
{

    private final BookService bookService;

    @Operation(
            summary = "Retrieve a Book by Id",
            description = "Get a Book object by specifying its id.",
           tags = {"book"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findBookById(@Parameter(description = "Book ID", required = true) @PathVariable Long id) {
        BookDTO bookDTO = bookService.findBookById(id);
        return ResponseEntity.ok(bookDTO);
    }

    @Operation(
            summary = "Create a Book",
            description = "Create a new Book object.",
           tags = {"book"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Parameter(description = "Create Book", required = true) @RequestBody @Validated BookDTO bookDTO) {
        BookDTO createdBook = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @Operation(
            summary = "Update a Book",
            description = "Update an existing Book object.",
           tags = {"book"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping
    public ResponseEntity<BookDTO> updateBook(@Parameter(description = "Update Book", required = true) @RequestBody @Validated BookDTO bookDTO) {
        BookDTO updatedBook = bookService.updateBook(bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(
            summary = "Assign a Book to a Library",
            description = "Assign a Book to a Library by specifying their ids.",
           tags = {"book"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book assigned to library", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Book or Library not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PatchMapping("/assign/{libraryId}/{bookId}")
    public ResponseEntity<BookDTO> assignBookToLibrary(@Parameter(description = "Library ID", required = true) @PathVariable Long libraryId, @Parameter(description = "Book ID", required = true) @PathVariable Long bookId)
    {
        BookDTO assignedBook = bookService.assignBookToLibrary(libraryId, bookId);
        return ResponseEntity.ok(assignedBook);
    }

    @Operation(
            summary = "Remove a Book from a Library",
            description = "Remove a Book from a Library by specifying their ids.",
           tags = {"book"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book removed from library"),
            @ApiResponse(responseCode = "404", description = "Book or Library not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PatchMapping("/remove/{libraryId}/{bookId}")
    public ResponseEntity<Void> removeBookFromLibrary(@Parameter(description = "Library ID", required = true) @PathVariable Long libraryId, @Parameter(description = "Book ID", required = true) @PathVariable Long bookId)
    {
        bookService.removeBookFromLibrary(libraryId, bookId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete a Book by Id",
            description = "Delete a Book object by specifying its id.",
           tags = {"book"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@Parameter(description = "Library ID", required = true) @PathVariable Long id)
    {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}