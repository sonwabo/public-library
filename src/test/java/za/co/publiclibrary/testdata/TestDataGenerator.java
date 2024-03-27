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

    COMPLETE(){
        @Override
        public LibraryDTO generate()
        {
            return new LibraryDTO(null,"Atlanta ATL Public Library","Downtown Atlanta",
                     new ArrayList<BookDTO>(){{
                         add(new BookDTO(null, "Greenlights", "Matthew McConaughey", MEMOIR, LocalDate.of(2023, 1, 18)));
                         add(new BookDTO(null, "Becoming", "Michelle Obama", MEMOIR, LocalDate.of(2018, 2, 4)));
                         add(new BookDTO(null, "Priestdaddy", "Patricia Lockwood", MEMOIR, LocalDate.of(2017, 3, 7)));
                         add(new BookDTO(null, "The Glass Castle","Jeannette Walls", MEMOIR, LocalDate.of(2005, 4, 30)));
                         add(new BookDTO(null, "know My Name", "Chanel Miller", MEMOIR, LocalDate.of(2023, 5, 28)));
                         add(new BookDTO(null, "The Liars' Club", "Mary Karr",  MEMOIR, LocalDate.of(1995, 6, 11)));
                     }},

                     new ArrayList<LibraryHoursDTO>(){{
                        add(new LibraryHoursDTO(null, MONDAY, "09:00am", "19:00pm"));
                        add(new LibraryHoursDTO(null, TUESDAY, "09:00am", "19:00pm"));
                        add(new LibraryHoursDTO(null, WEDNESDAY, "09:00am", "19:00pm"));
                        add(new LibraryHoursDTO(null, THURSDAY, "09:00am", "19:00pm"));
                        add(new LibraryHoursDTO(null, FRIDAY, "09:00am", "17:00pm"));
                        add(new LibraryHoursDTO(null, SATURDAY, "11:00am", "15:00pm"));
                    }});
        }
    }
    ,PARTIAL(){
        @Override
        public LibraryDTO generate()
        {
            return new LibraryDTO(null,"Harlem Public Library","New York (NYC) ", null , null);
        }
    }
    ,INCOMPLETE(){
        @Override
        public LibraryDTO generate()
        {
            return new LibraryDTO(null,"Texas Public Library",null, null , null);
        }
    };

    public abstract LibraryDTO generate();
}