package za.co.publiclibrary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import za.co.publiclibrary.dto.BookDTO;
import za.co.publiclibrary.dto.LibraryDTO;
import za.co.publiclibrary.dto.LibraryHoursDTO;
import za.co.publiclibrary.model.entity.LibraryEntity;
import za.co.publiclibrary.model.entity.BookEntity;
import za.co.publiclibrary.model.entity.LibraryHoursEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/26
 */

@Mapper
public interface LibraryMapper
{
    LibraryMapper INSTANCE = Mappers.getMapper(LibraryMapper.class);

    LibraryDTO toDTO(LibraryEntity entity);

    @Mappings({
            @Mapping(target = "withId", source = "id"),
            @Mapping(target = "withName", source = "name"),
            @Mapping(target = "withLocation", source = "location"),
            @Mapping(target = "withBookList", expression = "java(mapBookList(dto))"),
            @Mapping(target = "withOpeningHours", expression = "java(mapLibraryHours(dto))"),
    })
    LibraryEntity toEntity(LibraryDTO dto);

    @Mappings({
            @Mapping(target = "library_id", expression = "java(mapLibraryId(entity))")
    })
    BookDTO toDTO(BookEntity entity);
    default Long mapLibraryId(BookEntity bookEntity) {
        return bookEntity != null && bookEntity.getLibrary() != null ? bookEntity.getLibrary().getId() : null;
    }

    @Mappings({
            @Mapping(target = "library_id", expression = "java(mapLibraryId(entity))")
    })
    LibraryHoursDTO toDTO(LibraryHoursEntity entity);
    default Long mapLibraryId(final LibraryHoursEntity libraryHoursEntity)
    {
        return libraryHoursEntity != null && libraryHoursEntity.getLibrary() != null ? libraryHoursEntity.getLibrary().getId() : null;
    }

    default void mapLibraryOnDependencies(final LibraryEntity entity)
    {
        if (entity.getBookList() != null) {
            entity.getBookList().forEach(bookEntity -> bookEntity.setLibrary(entity));
        }
        if (entity.getOpeningHours() != null) {
            entity.getOpeningHours().forEach(libraryHoursEntity -> libraryHoursEntity.setLibrary(entity));
        }
    }

    default List<BookEntity> mapBookList(final LibraryDTO dto)
    {
      var books = new ArrayList<BookEntity>();
      if (dto.bookList() != null) {
          dto.bookList().forEach(bookDTO -> {
              books.add(
                      BookEntity.builder()
                              .withId(bookDTO.id())
                              .withAuthor(bookDTO.author())
                              .withTitle(bookDTO.title())
                              .withGenre(bookDTO.genre())
                              .withPublicationDate(bookDTO.publicationDate())
                              .build()
              );
          });
      }
      return books;
    }

    default List<LibraryHoursEntity> mapLibraryHours(final LibraryDTO dto)
    {
        var openingClosingHours = new ArrayList<LibraryHoursEntity>();
        if (dto.openingHours() != null) {
            dto.openingHours().forEach(libraryHoursDTO -> {
                openingClosingHours.add( LibraryHoursEntity.builder()
                                .withId(libraryHoursDTO.id())
                                .withDayOfWeek(libraryHoursDTO.dayOfWeek())
                                .withOpeningHours(libraryHoursDTO.openingHours())
                                .withClosingHours(libraryHoursDTO.closingHours())
                        .build());
            });
        }
        return openingClosingHours;
    }
}