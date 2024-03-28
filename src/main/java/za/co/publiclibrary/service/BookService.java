package za.co.publiclibrary.service;

import jakarta.validation.constraints.NotNull;
import za.co.publiclibrary.dto.BookDTO;
import za.co.publiclibrary.exceptions.BookNotFoundException;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/27
 */

public interface BookService {

    BookDTO findBookById(@NotNull final Long id) throws BookNotFoundException;

    BookDTO createBook(@NotNull final BookDTO bookDTO);

    BookDTO updateBook(@NotNull final BookDTO bookDTO);

    BookDTO assignBookToLibrary(@NotNull final Long library_id, final Long book);

    void removeBookFromLibrary(@NotNull final Long library, @NotNull final Long book);

    void deleteBookById(@NotNull final Long id);

}
