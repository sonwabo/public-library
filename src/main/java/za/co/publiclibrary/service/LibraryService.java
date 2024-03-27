package za.co.publiclibrary.service;

import za.co.publiclibrary.dto.LibraryDTO;
import za.co.publiclibrary.exceptions.LibraryNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/26
 */

public interface LibraryService
{
    List<LibraryDTO> getAllLibraries();

    Optional<LibraryDTO> findLibraryById(final Long id) throws LibraryNotFoundException;

    LibraryDTO createLibrary(final LibraryDTO libraryDTO);

    Optional<LibraryDTO> updateLibrary(final LibraryDTO libraryDTO) throws LibraryNotFoundException ;

    void deleteLibraryById(final Long id);
}