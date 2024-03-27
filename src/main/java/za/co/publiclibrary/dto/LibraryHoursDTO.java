package za.co.publiclibrary.dto;

import java.io.Serializable;
import java.time.DayOfWeek;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/27
  */

public record LibraryHoursDTO(Long id, DayOfWeek dayOfWeek, String openingHours, String closingHours) implements Serializable {}
