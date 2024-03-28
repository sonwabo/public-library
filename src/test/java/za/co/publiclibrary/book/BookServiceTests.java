package za.co.publiclibrary.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.publiclibrary.dto.BookDTO;
import za.co.publiclibrary.exceptions.BookNotFoundException;
import za.co.publiclibrary.exceptions.LibraryNotFoundException;
import za.co.publiclibrary.model.dao.BookRepository;
import za.co.publiclibrary.model.dao.LibraryRepository;
import za.co.publiclibrary.model.entity.LibraryEntity;
import za.co.publiclibrary.model.enums.BookGenre;
import za.co.publiclibrary.model.entity.BookEntity;
import za.co.publiclibrary.service.BookServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/28
 */

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private LibraryRepository libraryRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void test_FindBookById()
    {
        final var bookId = 1L;
        final var bookEntity = BookEntity.builder()
                .withId(bookId)
                .withAuthor("Author")
                .withTitle("Title")
                .withGenre(BookGenre.SCIENCE_FICTION)
                .withPublicationDate(LocalDate.now())
                .build();

        final var expectedBookDTO = new BookDTO(bookId, null, "Title", "Author", BookGenre.SCIENCE_FICTION, LocalDate.now());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));

        final var actualBookDTO = bookService.findBookById(bookId);

        assertEquals(expectedBookDTO, actualBookDTO);
    }

    @Test
    public void test_FindBookById_Negative()
    {
        final Long bookId = 31L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        final var ex = assertThrows(BookNotFoundException.class, () -> bookService.findBookById(bookId));

        assertEquals("Book not found for id: 31", ex.getMessage());
    }

    @Test
    public void test_CreateBook()
    {
        final var bookId = 5L;
        final var bookDTO = new BookDTO(null, null, "Title", "Author", BookGenre.SCIENCE_FICTION, LocalDate.now());
        final var savedEntity = BookEntity.builder()
                .withId(bookId)
                .withAuthor("Author")
                .withTitle("Title")
                .withGenre(BookGenre.SCIENCE_FICTION)
                .withPublicationDate(LocalDate.now())
                .build();

        when(bookRepository.save(any(BookEntity.class))).thenReturn(savedEntity);

        final var createdBook = bookService.createBook(bookDTO);

        assertNotNull(createdBook.id());
        assertEquals(bookDTO.title(), createdBook.title());
        assertEquals(bookDTO.author(), createdBook.author());
        assertEquals(bookDTO.genre(), createdBook.genre());
    }

    @Test
    public void test_UpdateBook_Positive() {

        final var bookId = 1L;
        final var bookDTO = new BookDTO(bookId, null, "Updated Title", "Updated Author", BookGenre.SCIENCE_FICTION, LocalDate.now());

        when(bookRepository.existsById(bookId)).thenReturn(true);

        // Mock saving logic to return the argument passed from the mapstruct mapping
        when(bookRepository.save(any(BookEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        final var updatedBook = bookService.updateBook(bookDTO);

        assertEquals(bookDTO, updatedBook);
    }

    @Test
    public void test_UpdateBook_Negative()
    {
        final var bookId = 1L;
        final var bookDTO = new BookDTO(bookId, null, "Updated Title", "Updated Author", BookGenre.SCIENCE_FICTION, LocalDate.now());

        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookDTO));
    }

    @Test
    public void test_AssignBookToLibrary_Positive()
    {
        final var libraryId = 1L;
        final var bookId = 1L;

        final var bookEntity = BookEntity.builder().withId(bookId).build();
        final var libraryEntity = LibraryEntity.builder().withId(libraryId).build();

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(libraryEntity));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        when(bookRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        final var assignedBook = bookService.assignBookToLibrary(libraryId, bookId);

        assertEquals(libraryId, assignedBook.library_id());
        assertEquals(bookId, assignedBook.id());
    }

    @Test
    public void test_AssignBookToLibrary_Negative_LibraryNotFound()
    {
        final var libraryId = 1L;
        final var bookId = 1L;

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.empty());

        assertThrows(LibraryNotFoundException.class, () -> bookService.assignBookToLibrary(libraryId, bookId));
    }

    @Test
    public void test_AssignBookToLibrary_Negative_BookNotFound()
    {
        final var libraryId = 1L;
        final var bookId = 1L;

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(new LibraryEntity()));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.assignBookToLibrary(libraryId, bookId));
    }

    @Test
    public void test_RemoveBookFromLibrary_Positive()
    {
        final var libraryId = 1L;
        final var bookId = 1L;

        final var libraryEntity = LibraryEntity.builder()
                .withId(libraryId)
                .build();

        final var bookEntity = BookEntity.builder()
                .withId(bookId)
                .withLibrary(libraryEntity)
                .build();

        libraryEntity.setBookList(new ArrayList<>());
        libraryEntity.getBookList().add(bookEntity);

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(libraryEntity));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));

        bookService.removeBookFromLibrary(libraryId, bookId);

        assertNull(bookEntity.getLibrary());
    }

    @Test
    public void test_RemoveBookFromLibrary_Negative_LibraryNotFound()
    {
        final var libraryId = 1L;
        final var bookId = 1L;

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.empty());

        assertThrows(LibraryNotFoundException.class, () -> bookService.removeBookFromLibrary(libraryId, bookId));
    }

    @Test
    public void test_RemoveBookFromLibrary_Negative_BookNotFound()
    {
        final var libraryId = 1L;
        final var bookId = 1L;

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(new LibraryEntity()));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.removeBookFromLibrary(libraryId, bookId));
    }

    @Test
    public void test_DeleteBookById()
    {
        final var bookId = 1L;

        bookService.deleteBookById(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    public void testEvictCache()
    {
        assertDoesNotThrow(() -> bookService.evictCache());
    }

}
