package za.co.publiclibrary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.DayOfWeek;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/27
  */
@JsonIgnoreProperties
public record LibraryHoursDTO(Long id, @NotNull Long library_id, @NotNull DayOfWeek dayOfWeek, @NotNull String openingHours, @NotNull String closingHours) implements Serializable
{
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LibraryHoursDTO that = (LibraryHoursDTO) o;

        return new EqualsBuilder().append(id, that.id).append(dayOfWeek, that.dayOfWeek).append(openingHours, that.openingHours).append(closingHours, that.closingHours).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(id).append(dayOfWeek).append(openingHours).append(closingHours).toHashCode();
    }
}