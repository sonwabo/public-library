package za.co.publiclibrary.controller;

import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.util.UriComponentsBuilder;
import za.co.publiclibrary.dto.LibraryDTO;
import za.co.publiclibrary.service.LibraryService;

import java.util.List;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/26
 */

@RestController
@RequestMapping(LibraryController.BASE_PATH)
@RequiredArgsConstructor
public class LibraryController {
    final static String BASE_PATH = "/api/libraries";

    private final LibraryService libraryService;

    @ApiOperation("Get all libraries")
    @GetMapping
    public ResponseEntity<List<LibraryDTO>> getAllLibraries()
    {
        return ResponseEntity
                .ok(this.libraryService.getAllLibraries());
    }

    @PostMapping
    public ResponseEntity<LibraryDTO> createLibrary(@RequestBody final LibraryDTO library, UriComponentsBuilder uriComponentsBuilder)
    {
        final var savedLibrary = this.libraryService.createLibrary(library);

//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString());

        final var location = uriComponentsBuilder
                .path(BASE_PATH)
                .buildAndExpand(savedLibrary.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(savedLibrary);
    }

    @PutMapping
    @SneakyThrows
    public ResponseEntity<Void> updateLibrary(@RequestBody LibraryDTO library)
    {
        return libraryService
                .updateLibrary(library)
                .map(updated -> ResponseEntity.ok().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibraryById(@PathVariable Long id)
    {
        libraryService.deleteLibraryById(id);
        return ResponseEntity.ok().build();
    }
}
