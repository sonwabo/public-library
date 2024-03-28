package za.co.publiclibrary.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import za.co.publiclibrary.IntegrationTestParent;
import za.co.publiclibrary.controller.BookController;
import za.co.publiclibrary.service.BookService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/28
 */

public class BookControllerITTests extends IntegrationTestParent {

    @Autowired
    private BookService bookService;

    @Test
    public void testFindBookById() throws Exception {
        final Long bookId = 1L;

        mvc.perform(get(BookController.BASE_PATH + "/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(bookId));
    }

}
