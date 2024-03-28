package za.co.publiclibrary.libraries;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import za.co.publiclibrary.controller.LibraryController;
import za.co.publiclibrary.dto.LibraryDTO;
import za.co.publiclibrary.service.LibraryService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/27
 */

@ExtendWith(MockitoExtension.class)
public class LibraryControllerTests
{
    @InjectMocks
    LibraryController subject;
    @Mock
    LibraryService service;

    @Test
    public void test_getAllLibraries(/*Should return all libraries when getAllLibraries() is called*/)
    {
        final var expectedLibraries = new ArrayList<LibraryDTO>(){{
            add(new LibraryDTO(1L, "Library 1", "Location 1", new ArrayList<>(), new ArrayList<>()));
            add(new LibraryDTO(2L, "Library 2", "Location 2", new ArrayList<>(), new ArrayList<>()));
        }};

        when(service.getAllLibraries()).thenReturn( expectedLibraries );

        ResponseEntity<List<LibraryDTO>> response = subject.getAllLibraries();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedLibraries, response.getBody());
    }

    @Test
    public void test_getAllLibraries_emptyList(/*Should return an empty list when getAllLibraries() is called and there are no libraries*/)
    {

        final var expectedLibraries = new ArrayList<LibraryDTO>();
        when(service.getAllLibraries()).thenReturn(expectedLibraries);

        ResponseEntity<List<LibraryDTO>> response = subject.getAllLibraries();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedLibraries, response.getBody());
    }

    @Test
    @SneakyThrows
    public void test_getLibrary_withValidId(/*Should return a specific library when getLibrary(id) is called with a valid id*/)
    {
        final Long id = 1L;
        final var expectedLibrary = new LibraryDTO(id, "Library 1", "Location 1", new ArrayList<>(), new ArrayList<>());
        when(service.findLibraryById(id)).thenReturn(expectedLibrary);

        ResponseEntity<LibraryDTO> response = subject.getLibrary(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedLibrary, response.getBody());
        assertEquals(expectedLibrary.id(), response.getBody().id());


        verify(service, times(1)).findLibraryById(any(Long.class));

    }

    @Test
    public void test_createLibrary_withValidLibrary(/*Should create a new library when createLibrary(library) is called with a valid library object*/)
    {
        final var request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        final var library = new LibraryDTO(1L, "Library 1", "Location 1", new ArrayList<>(), new ArrayList<>());
        when(service.createLibrary(library)).thenReturn(library);

        ResponseEntity<LibraryDTO> response = subject.createLibrary(library);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(library, response.getBody());
        assertEquals(library.id(), response.getBody().id());
        assertEquals(library.name(), response.getBody().name());
        assertEquals(library.location(), response.getBody().location());

        verify(service, times(1)).createLibrary(any(LibraryDTO.class));
    }

    @Test
    @SneakyThrows
    public void test_updateLibrary_withValidLibrary()
    {
        final var library = new LibraryDTO(1L, "Library 1", "Location 1", null, null);
        when(service.updateLibrary(library)).thenReturn(library);

        ResponseEntity<LibraryDTO> response = subject.updateLibrary(library);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(library, response.getBody());

        verify(service, times(1)).updateLibrary(any(LibraryDTO.class));

    }

    @Test
    public void test_deleteLibrary_withValidId()
    {
        ResponseEntity<Void> response = subject.deleteLibraryById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}