package za.co.publiclibrary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import za.co.publiclibrary.dto.LibraryDTO;
import za.co.publiclibrary.model.entity.LibraryEntity;

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

    LibraryEntity toEntity(LibraryDTO dto);
}