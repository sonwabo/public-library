package za.co.publiclibrary.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import za.co.publiclibrary.dto.BookDTO;

public interface IBookControllerV1 {

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
    ResponseEntity<BookDTO> findBookById(Long id);


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
    ResponseEntity<BookDTO> createBook(BookDTO bookDTO);

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
    ResponseEntity<BookDTO> updateBook(BookDTO bookDTO);

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
    ResponseEntity<BookDTO> assignBookToLibrary(Long libraryId, Long bookId);

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
    ResponseEntity<Void> removeBookFromLibrary(Long libraryId, Long bookId);

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
    ResponseEntity<Void> deleteBookById(Long id);
}