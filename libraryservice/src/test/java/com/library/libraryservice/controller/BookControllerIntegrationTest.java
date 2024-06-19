package com.library.libraryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraryservice.LibraryServiceApplication;
import com.library.libraryservice.dto.BooksDTO;
import com.library.libraryservice.repository.BookRepository;
import com.library.libraryservice.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LibraryServiceApplication.class)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        bookRepository.deleteAll();
    }

    @Test
    public void testGetAllBooks() throws Exception {
        BooksDTO book1 = new BooksDTO(1L, "Book One", "Category One", 10, true, null);
        BooksDTO book2 = new BooksDTO(2L, "Book Two", "Category Two", 5, true, null);
        when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book One"))
                .andExpect(jsonPath("$[1].title").value("Book Two"));
    }

    @Test
    public void testCreateBook() throws Exception {
        BooksDTO bookDTO = new BooksDTO(null, "Book One", "Category One", 10, true, null);

        when(bookService.saveBook(Mockito.any(BooksDTO.class))).thenAnswer(invocation -> {
            BooksDTO newBookDTO = invocation.getArgument(0);
            return new BooksDTO(1L, newBookDTO.getTitle(), newBookDTO.getCategory(), newBookDTO.getQuantity(),
                    newBookDTO.isAvailable(), null);
        });

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Book One"))
                .andExpect(jsonPath("$.category").value("Category One"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        BooksDTO bookDTO = new BooksDTO(null, "Book One", "Category One", 10, true, null);
        bookDTO.setId(1L);
        when(bookService.updateBook(Mockito.anyLong(), Mockito.any(BooksDTO.class))).thenAnswer(invocation -> {
            BooksDTO updatedBookDTO = invocation.getArgument(1);
            return new BooksDTO(updatedBookDTO.getId(), updatedBookDTO.getTitle(), updatedBookDTO.getCategory(),
                    updatedBookDTO.getQuantity(), updatedBookDTO.isAvailable(), null);
        });

        mockMvc.perform(put("/books/{id}", bookDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book One"))
                .andExpect(jsonPath("$.category").value("Category One"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        // Create a mock BooksDTO to save
        BooksDTO bookDTO = new BooksDTO(null, "Book One", "Category One", 10, true, null);
        BooksDTO savedBookDTO = new BooksDTO(1L, "Book One", "Category One", 10, true, null);

        // Mock the behavior of saveBook method in BookService
        when(bookService.saveBook(bookDTO)).thenReturn(savedBookDTO);

        // Perform the deletion request
        mockMvc.perform(delete("/books/{id}", savedBookDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
