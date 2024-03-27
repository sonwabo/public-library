package za.co.publiclibrary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/26
 */
@JsonIgnoreProperties
public record LibraryDTO(Long id, @NotNull String name, @NotNull String location, List<BookDTO> bookList, List<LibraryHoursDTO> openingHours) implements Serializable
{
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LibraryDTO that = (LibraryDTO) o;

        return new EqualsBuilder().append(id, that.id).append(name, that.name).append(location, that.location).append(bookList, that.bookList).append(openingHours, that.openingHours).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(location).append(bookList).append(openingHours).toHashCode();
    }
}
