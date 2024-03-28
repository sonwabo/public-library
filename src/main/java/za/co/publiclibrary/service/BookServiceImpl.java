package za.co.publiclibrary.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.publiclibrary.dto.BookDTO;
import za.co.publiclibrary.exceptions.BookNotFoundException;
import za.co.publiclibrary.exceptions.LibraryNotFoundException;
import za.co.publiclibrary.mapper.BookMapper;
import za.co.publiclibrary.model.dao.BookRepository;
import za.co.publiclibrary.model.dao.LibraryRepository;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/27
 */

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;

    @Cacheable(value = "books", key = "#id")
    public BookDTO findBookById(final Long id)
    {
        return bookRepository.findById(id)
                .map(BookMapper.INSTANCE::toDTO)
                .orElseThrow(() -> new BookNotFoundException("Book not found for id: " + id));
    }

    @CacheEvict(value = "books", allEntries = true)
    public BookDTO createBook(final BookDTO bookDTO)
    {
        final var entity = BookMapper.INSTANCE.toEntity(bookDTO);
        return BookMapper.INSTANCE.toDTO( bookRepository.save(entity));
    }

    @CacheEvict(value = "books", allEntries = true)
    public BookDTO updateBook(final BookDTO bookDTO)
    {
        validateBookExists(bookDTO.id());
        final var entity = BookMapper.INSTANCE.toEntity(bookDTO);
        return BookMapper.INSTANCE.toDTO( bookRepository.save(entity));
    }

    @CacheEvict(value = "books", allEntries = true)
    @SneakyThrows
    public BookDTO assignBookToLibrary(final Long libraryId, final Long bookId)
    {
        final var library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found for id: " + libraryId));

        final var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found for id: " + bookId));

        book.setLibrary(library);

        return BookMapper.INSTANCE.toDTO(bookRepository.save(book));
    }

    @CacheEvict(value = "books", allEntries = true)
    public void removeBookFromLibrary(final Long libraryId, final Long bookId)
    {
        final var  library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found for id: " + libraryId));

        final var  book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found for id: " + bookId));

        if (library.getBookList() != null && !library.getBookList().contains(book)) {
            throw new IllegalArgumentException(String.format("Book with id:%s is not associated with Library with id:%s", bookId, libraryId));
        }

        book.setLibrary(null);
    }

    @CacheEvict(value = "books", allEntries = true)
    public void deleteBookById(final Long id)
    {
        bookRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Refresh cache daily at midnight
    @CacheEvict(value = "books", allEntries = true)
    public void evictCache() {
        // Cache will be refreshed automatically
    }
    private void validateBookExists(final Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Book not found for id: " + bookId);
        }
    }
}