package za.co.publiclibrary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.co.publiclibrary.dto.LibraryDTO;
import za.co.publiclibrary.service.LibraryService;

import java.util.List;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/26
 */

@RestController
@RequestMapping( "/api/library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @Operation(
            summary = "Get all libraries",
            description = "Retrieve all libraries.",
            tags = {"library"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of libraries"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<List<LibraryDTO>> getAllLibraries()
    {
        return ResponseEntity
                .ok(this.libraryService.getAllLibraries());
    }

    @Operation(
            summary = "Get a library by ID",
            description = "Retrieve a library by specifying its ID.",
            tags = {"library"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Library found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LibraryDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Library not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{id}")
    @SneakyThrows
    public ResponseEntity<LibraryDTO> getLibrary(@Parameter(description = "Library Id", required = true) @PathVariable final Long id)
    {
        return ResponseEntity.ok(this.libraryService.findLibraryById(id));
    }

    @Operation(
            summary = "Create a new library",
            description = "Create a new library.",
            tags = {"library"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Library created successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LibraryDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<LibraryDTO> createLibrary(@Parameter(description  = "Library details", required = true) @Valid @RequestBody final LibraryDTO library)
    {
        final var savedLibrary = this.libraryService.createLibrary(library);

        final var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/api/libraries")
                .buildAndExpand(savedLibrary.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(savedLibrary);
    }

    @Operation(
            summary = "Update an existing library",
            description = "Update an existing library.",
            tags = {"library"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Library updated successfully", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LibraryDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Library not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping
    @SneakyThrows
    public ResponseEntity<LibraryDTO> updateLibrary(@Parameter(description  = "Updated library details", required = true) @Valid @RequestBody final LibraryDTO library)
    {
        return ResponseEntity
                .ok(this.libraryService.updateLibrary(library));
    }


    @Operation(
            summary = "Delete a library by Id",
            description = "Delete a library by specifying its ID.",
            tags = {"library"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Library deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Library not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibraryById(@Parameter(description  = "Library ID", required = true) @PathVariable final Long id)
    {
        libraryService.deleteLibraryById(id);
        return ResponseEntity.ok().build();
    }
}