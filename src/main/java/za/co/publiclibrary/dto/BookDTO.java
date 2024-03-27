package za.co.publiclibrary.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import za.co.publiclibrary.model.enums.BookGenre;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/26
 */

public record BookDTO(Long id, String title, String author, BookGenre genre, LocalDate publicationDate) implements Serializable
{
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BookDTO bookDTO = (BookDTO) o;

        return new EqualsBuilder().append(id, bookDTO.id).append(title, bookDTO.title).append(author, bookDTO.author).append(publicationDate, bookDTO.publicationDate).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(id).append(title).append(author).append(publicationDate).toHashCode();
    }
}
