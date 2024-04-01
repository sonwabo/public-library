package za.co.publiclibrary.book;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import za.co.publiclibrary.IntegrationTestParent;
import za.co.publiclibrary.dto.BookDTO;
import za.co.publiclibrary.service.BookService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.publiclibrary.model.enums.BookGenre.MANGA;
import static za.co.publiclibrary.model.enums.BookGenre.MEMOIR;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/28
 */

class BookControllerITTests extends IntegrationTestParent {

    final static String BASE_PATH = "/api/books";

    @Test
    void testFindBookById() throws Exception
    {
        final var bookId = 1L;

        mvc.perform(get(BASE_PATH + "/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(bookId));
    }

    @Test
    void testCreateBook() throws Exception
    {
        final var requestBody = new BookDTO(null,
                null,
                "know My Name Version 2",
                "Chanel Miller",
                MEMOIR,
                LocalDate.of(2024,03,28));

        mvc.perform(post(BASE_PATH)
                        .content(asJsonString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("know My Name Version 2"))
                .andExpect(jsonPath("$.author").value("Chanel Miller"));
    }

    @Test
    void testUpdateBook() throws Exception
    {
        final var bookId = 1L;
        final var requestBody = new BookDTO(bookId,
                null,
                "Updated Book",
                "Chanel Miller",
                MEMOIR,
                LocalDate.of(2024,03,28));

        mvc.perform(put(BASE_PATH)
                        .content(asJsonString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author").value("Chanel Miller"))
                .andExpect(jsonPath("$.genre").value("MEMOIR"));
    }

    @Test
    void testAssignBookToLibrary() throws Exception
    {
        final var libraryId = 1L;
        final var bookId = 1L;

        mvc.perform(patch(BASE_PATH + "/assign/{libraryId}/{bookId}", libraryId, bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.library_id").value(libraryId));
    }

    @Test
    void testUpdateAndRemoveBook() throws Exception
    {

        final var libraryId = 1L;

        var requestBody = new BookDTO(null,
                libraryId,
                "know My Name Version 2",
                "Chanel Miller",
                MEMOIR,
                LocalDate.of(2024,03,28));

        // Create a new book
        final var createResult = mvc.perform(post(BASE_PATH)
                        .content(asJsonString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract the book ID from the response
        final var createResponse = createResult.getResponse().getContentAsString();
        final var bookId = Long.parseLong(JsonPath.parse(createResponse).read("$.id").toString());

        // Update the book
        var updateBook = new BookDTO(bookId,
                libraryId,
                "know My Name Version 5.0",
                "Chanel Miller",
                MEMOIR,
                LocalDate.of(2024,03,28));

        mvc.perform(put(BASE_PATH)
                        .content(asJsonString(updateBook))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Remove the book
        mvc.perform(patch(BASE_PATH + "/remove/{libraryId}/{bookId}", libraryId, bookId))
                .andExpect(status().isNoContent());

        // Delete the book
        mvc.perform(delete(BASE_PATH + "/{id}", bookId))
                .andExpect(status().is2xxSuccessful());

        // Verify the book is no longer accessible
        mvc.perform(get(BASE_PATH + "/{id}", bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindBookById_Negative_NotFound() throws Exception
    {
        final var nonExistingBookId = 12L;

        mvc.perform(get(BASE_PATH + "/{id}", nonExistingBookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateBook_Negative_InvalidId() throws Exception
    {

        final var requestBody = new BookDTO(115L,
                null,
                "My hero academia",
                "Jin Woo",
                null,
                null);

        mvc.perform(put(BASE_PATH)
                        .content(asJsonString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAssignBookToLibrary_Negative_BookNotFound() throws Exception
    {
        final var nonExistingBookId = 30L;
        final var libraryId = 1L;

        mvc.perform(patch(BASE_PATH + "/assign/{libraryId}/{bookId}", libraryId, nonExistingBookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveBookFromLibrary_Negative_BookNotFound() throws Exception
    {
        final var nonExistingBookId = 445L;
        final var libraryId = 1L;

        mvc.perform(patch(BASE_PATH + "/remove/{libraryId}/{bookId}", libraryId, nonExistingBookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBookById_Negative_NotFound() throws Exception
    {
        final var nonExistingBookId = 22L;

        mvc.perform(delete(BASE_PATH + "/{id}", nonExistingBookId))
                .andExpect(status().isNotFound());
    }
}
