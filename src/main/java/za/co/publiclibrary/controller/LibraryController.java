package za.co.publiclibrary.controller;

import io.swagger.annotations.ApiOperation;
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
@RequestMapping( "/api/libraries")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @ApiOperation("Get all libraries")
    @GetMapping
    public ResponseEntity<List<LibraryDTO>> getAllLibraries()
    {
        return ResponseEntity
                .ok(this.libraryService.getAllLibraries());
    }

    @GetMapping("/{id}")
    @SneakyThrows
    public ResponseEntity<LibraryDTO> getLibrary(@PathVariable final Long id)
    {
        return ResponseEntity.ok(this.libraryService.findLibraryById(id));
    }

    @PostMapping
    public ResponseEntity<LibraryDTO> createLibrary(@Valid @RequestBody final LibraryDTO library)
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

    @PutMapping
    @SneakyThrows
    public ResponseEntity<LibraryDTO> updateLibrary(@Valid @RequestBody final LibraryDTO library)
    {
        return ResponseEntity
                .ok(this.libraryService.updateLibrary(library));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibraryById(@PathVariable final Long id)
    {
        libraryService.deleteLibraryById(id);
        return ResponseEntity.ok().build();
    }
}