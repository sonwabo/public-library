package za.co.publiclibrary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import za.co.publiclibrary.dto.BookDTO;
import za.co.publiclibrary.model.entity.BookEntity;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/28
 */

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
     @Mappings({
             @Mapping(target = "withId", source = "id"),
             @Mapping(target = "withAuthor", source = "author"),
             @Mapping(target = "withTitle", source = "title"),
             @Mapping(target = "withGenre", source = "genre"),
             @Mapping(target = "withPublicationDate", source = "publicationDate"),
             @Mapping(target = "withLibrary", ignore = true)
     })
     BookEntity toEntity(BookDTO dto);

    @Mapping(target = "library_id", expression = "java(mapLibraryId(entity))")
    BookDTO toDTO(BookEntity entity);

    default Long mapLibraryId(final BookEntity book)
    {
         return book != null && book.getLibrary() != null ? book.getLibrary().getId() : null;
    }
}
