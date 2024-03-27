package za.co.publiclibrary.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import za.co.publiclibrary.controller.LibraryController;
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

@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerITTests {

    final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private LibraryService service;

    {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void getAllLibrariesAPI() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                        .get(LibraryController.BASE_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]").exists());
    }

    @Test
    public void getLibraryAPI() throws Exception
    {
        this.mvc.perform(MockMvcRequestBuilders
                        .get(LibraryController.BASE_PATH.concat("/1"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Germiston Public Library"))
                .andExpect(jsonPath("$.location").value("Gauteng East"));
    }

    @Test
    public void getLibraryAPI_throw_LibraryNotFoundException() throws Exception
    {
        final String expectedErrorMessage = "Library not found for provided id";

        mvc.perform(MockMvcRequestBuilders
                        .get(LibraryController.BASE_PATH.concat("/100"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedErrorMessage))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void createLibraryAPI() throws Exception {
        final var data = TestDataGenerator.COMPLETE.generate();

        mvc.perform(MockMvcRequestBuilders
                        .post(LibraryController.BASE_PATH)
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
    public void createLibraryAPI_ValidationErrorMethodArgumentNotValidException() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders
                        .post(LibraryController.BASE_PATH)
                        .content(asJsonString(TestDataGenerator.INCOMPLETE.generate()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("must not be null"));
    }


    public String asJsonString(final Object obj)
    {
        try {
            return this.objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}