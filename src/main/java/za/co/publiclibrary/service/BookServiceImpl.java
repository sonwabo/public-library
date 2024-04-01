package za.co.publiclibrary.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.publiclibrary.config.MessageUtil;
import za.co.publiclibrary.dto.BookDTO;
import za.co.publiclibrary.exceptions.BookNotFoundException;
import za.co.publiclibrary.exceptions.LibraryNotFoundException;
import za.co.publiclibrary.mapper.BookMapper;
import za.co.publiclibrary.model.dao.BookRepository;
import za.co.publiclibrary.model.dao.LibraryRepository;

import static za.co.publiclibrary.config.MessageUtil.ExceptionMsgKey.BOOK_NOT_FOUND;
import static za.co.publiclibrary.config.MessageUtil.ExceptionMsgKey.LIBRARY_NOT_FOUND;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/27
 */

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final MessageUtil messageUtil;

    @Cacheable(value = "books", key = "#id")
    public BookDTO findBookById(final Long id)
    {
        return bookRepository.findById(id)
                .map(BookMapper.INSTANCE::toDTO)
                .orElseThrow(() -> new BookNotFoundException(this.messageUtil.getMessage(BOOK_NOT_FOUND.value(),id)));
    }

    @CacheEvict(value = "books", allEntries = true)
    public BookDTO createBook(final BookDTO bookDTO)
    {
        final var entity = BookMapper.INSTANCE.toEntity(bookDTO);
        if (bookDTO.library_id() != null) {
            final var library = libraryRepository.findById(bookDTO.library_id())
                    .orElseThrow(() -> new LibraryNotFoundException(this.messageUtil.getMessage(LIBRARY_NOT_FOUND.value(), bookDTO.library_id())));
            entity.setLibrary(library);
            log.info("Adding Library with id{} to book", library.getId());
        }
        return BookMapper.INSTANCE.toDTO( bookRepository.save(entity));
    }

    @CacheEvict(value = "books", allEntries = true)
    public BookDTO updateBook(final BookDTO bookDTO)
    {
        validateBookExists(bookDTO.id());
        final var entity = BookMapper.INSTANCE.toEntity(bookDTO);

        /**
         * NOTE: Need to take a look at the mapping
         */
        if (bookDTO.library_id() != null && entity.getLibrary() == null /* This is to make sure the lib is not overwritten :: Hack**/) {
            final var library = libraryRepository.findById(bookDTO.library_id())
                    .orElseThrow(() -> new LibraryNotFoundException(this.messageUtil.getMessage(LIBRARY_NOT_FOUND.value(), bookDTO.library_id())));
            entity.setLibrary(library);
            log.info("Adding Library with id{} to book", library.getId());
        }
        return BookMapper.INSTANCE.toDTO( bookRepository.save(entity));
    }

    @CacheEvict(value = "books", allEntries = true)
    @SneakyThrows
    public BookDTO assignBookToLibrary(final Long libraryId, final Long bookId)
    {
        final var library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new LibraryNotFoundException(this.messageUtil.getMessage(LIBRARY_NOT_FOUND.value(), libraryId)));

        final var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(this.messageUtil.getMessage(BOOK_NOT_FOUND.value(),bookId)));

        book.setLibrary(library);

        return BookMapper.INSTANCE.toDTO(bookRepository.save(book));
    }

    @CacheEvict(value = "books", allEntries = true)
    public void removeBookFromLibrary(final Long libraryId, final Long bookId)
    {
        final var  library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new LibraryNotFoundException(this.messageUtil.getMessage(LIBRARY_NOT_FOUND.value(), libraryId)));

        final var  book = bookRepository.findById(bookId)
                .orElseThrow(() ->  new BookNotFoundException(this.messageUtil.getMessage(BOOK_NOT_FOUND.value(),bookId)));

        if (library.getBookList() != null && !library.getBookList().contains(book)) {
            throw new IllegalArgumentException(this.messageUtil.getMessage(BOOK_NOT_FOUND.value(),bookId,libraryId));
        }

        book.setLibrary(null);
    }

    @CacheEvict(value = "books", allEntries = true)
    public void deleteBookById(final Long id)
    {
        validateBookExists(id);
        bookRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Refresh cache daily at midnight
    @CacheEvict(value = "books", allEntries = true)
    public void evictCache() {
        // Cache will be refreshed automatically
    }
    private void validateBookExists(final Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(this.messageUtil.getMessage(BOOK_NOT_FOUND.value(),bookId));
        }
    }
}