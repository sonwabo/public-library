package za.co.publiclibrary.testdata;

import za.co.publiclibrary.dto.BookDTO;
import za.co.publiclibrary.dto.LibraryDTO;
import za.co.publiclibrary.dto.LibraryHoursDTO;

import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.DayOfWeek.*;
import static za.co.publiclibrary.model.enums.BookGenre.MEMOIR;


/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/27
 */

public enum TestDataGenerator {

    COMPLETE() {
        @Override
        public LibraryDTO generateLibrary(final Long library_id) {
            return new LibraryDTO(null, "Atlanta ATL Public Library", "Downtown Atlanta",
                    new ArrayList<BookDTO>() {{
                        add(new BookDTO(null, library_id, "Greenlights", "Matthew McConaughey", MEMOIR, LocalDate.of(2023, 1, 18)));
                        add(new BookDTO(null, library_id, "Becoming", "Michelle Obama", MEMOIR, LocalDate.of(2018, 2, 4)));
                        add(new BookDTO(null, library_id, "Priestdaddy", "Patricia Lockwood", MEMOIR, LocalDate.of(2017, 3, 7)));
                        add(new BookDTO(null, library_id, "The Glass Castle", "Jeannette Walls", MEMOIR, LocalDate.of(2005, 4, 30)));
                        add(new BookDTO(null, library_id, "know My Name", "Chanel Miller", MEMOIR, LocalDate.of(2023, 5, 28)));
                        add(new BookDTO(null, library_id, "The Liars' Club", "Mary Karr", MEMOIR, LocalDate.of(1995, 6, 11)));
                    }},

                    new ArrayList<LibraryHoursDTO>() {{
                        add(new LibraryHoursDTO(null, library_id, MONDAY, "09:00am", "19:00pm"));
                        add(new LibraryHoursDTO(null, library_id, TUESDAY, "09:00am", "19:00pm"));
                        add(new LibraryHoursDTO(null, library_id, WEDNESDAY, "09:00am", "19:00pm"));
                        add(new LibraryHoursDTO(null, library_id, THURSDAY, "09:00am", "19:00pm"));
                        add(new LibraryHoursDTO(null, library_id, FRIDAY, "09:00am", "17:00pm"));
                        add(new LibraryHoursDTO(null, library_id, SATURDAY, "11:00am", "15:00pm"));
                    }});
        }
    }, PARTIAL() {
        @Override
        public LibraryDTO generateLibrary(final Long library_id) {
            return new LibraryDTO(library_id, "Harlem Public Library", "New York (NYC) ", null, null);
        }
    }, INCOMPLETE() {
        @Override
        public LibraryDTO generateLibrary(final Long library_id) {
            return new LibraryDTO(library_id, "Texas Public Library", null, null, null);
        }
    };

    public abstract LibraryDTO generateLibrary(final Long library_id);
}