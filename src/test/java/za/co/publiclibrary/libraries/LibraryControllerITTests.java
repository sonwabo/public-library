package za.co.publiclibrary.libraries;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import za.co.publiclibrary.IntegrationTestParent;
import za.co.publiclibrary.service.LibraryService;
import za.co.publiclibrary.testdata.TestDataGenerator;

import java.time.DayOfWeek;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/27
 */

class LibraryControllerITTests extends IntegrationTestParent {

    final static String BASE_PATH = "/api/library";
    @Autowired
    private LibraryService service;

    @Test
    void getAllLibrariesAPI() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                        .get(BASE_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]").exists());
    }

    @Test
    void getLibraryAPI() throws Exception
    {
        this.mvc.perform(MockMvcRequestBuilders
                        .get(BASE_PATH.concat("/1"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Germiston Public Library"))
                .andExpect(jsonPath("$.location").value("Gauteng East"));
    }

    @Test
    void getLibraryAPI_throw_LibraryNotFoundException() throws Exception
    {
        final String expectedErrorMessage = "Library not found for provided id: 100";

        mvc.perform(MockMvcRequestBuilders
                        .get(BASE_PATH.concat("/100"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void createLibraryAPI() throws Exception {
        final var data = TestDataGenerator.COMPLETE.generateLibrary(null);

        mvc.perform(MockMvcRequestBuilders
                        .post(BASE_PATH)
                        .content(asJsonString(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookList[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.openingHours[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookList[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.openingHours[5].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.openingHours[5].dayOfWeek").value(DayOfWeek.SATURDAY.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.openingHours[5].openingHours").value("11:00am"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.openingHours[5].closingHours").value("15:00pm"));
    }

    @Test
    void createLibraryAPI_ValidationErrorMethodArgumentNotValidException() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .post(BASE_PATH)
                        .content(asJsonString(TestDataGenerator.INCOMPLETE.generateLibrary(null)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("must not be null"));
    }

    /*
    @Test
    void updateLibraryAPI() throws Exception {


        final var libraryDTO = TestDataGenerator.COMPLETE.generateLibrary(null);
        LibraryDTO createdLibrary = createLibrary(libraryDTO);

        // Update the library
        String newName = "Updated Library Name";
        createdLibrary.setName(newName);

        mvc.perform(MockMvcRequestBuilders
                        .put(BASE_PATH)
                        .content(asJsonString(createdLibrary))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdLibrary.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newName));
    }

    @Test
    void deleteLibraryByIdAPI() throws Exception {
        // Create a library
        LibraryDTO libraryDTO = TestDataGenerator.COMPLETE.generateLibrary(null);
        LibraryDTO createdLibrary = createLibrary(libraryDTO);

        // Delete the library
        mvc.perform(MockMvcRequestBuilders
                        .delete(BASE_PATH + "/" + createdLibrary.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Try to fetch the deleted library (it should not be found)
        mvc.perform(MockMvcRequestBuilders
                        .get(BASE_PATH + "/" + createdLibrary.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    void updateLibraryAPI_NonExistingLibrary() throws Exception {
        // Create a library DTO with an ID that doesn't exist
        LibraryDTO libraryDTO = TestDataGenerator.COMPLETE.generateLibrary(null);
        libraryDTO.setId(999L);

        // Attempt to update the non-existing library
        mvc.perform(MockMvcRequestBuilders
                        .put(BASE_PATH)
                        .content(asJsonString(libraryDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLibraryByIdAPI_NonExistingLibrary() throws Exception {
        // Attempt to delete a library with a non-existing ID
        Long nonExistingId = 999L;

        mvc.perform(MockMvcRequestBuilders
                        .delete(BASE_PATH + "/" + nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLibraryByIdAPI_LibraryInUse() throws Exception {
        // Create a library
        LibraryDTO libraryDTO = TestDataGenerator.COMPLETE.generateLibrary(null);
        LibraryDTO createdLibrary = createLibrary(libraryDTO);

        // Attempt to delete the library while it's still in use (e.g., it has books associated with it)
        mvc.perform(MockMvcRequestBuilders
                        .delete(BASE_PATH + "/" + createdLibrary.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    */


}