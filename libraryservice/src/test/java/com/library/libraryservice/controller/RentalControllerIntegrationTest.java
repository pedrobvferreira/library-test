package com.library.libraryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.libraryservice.LibraryServiceApplication;
import com.library.libraryservice.model.Books;
import com.library.libraryservice.model.Rentals;
import com.library.libraryservice.model.Users;
import com.library.libraryservice.enums.UserProfile;
import com.library.libraryservice.repository.BookRepository;
import com.library.libraryservice.repository.RentalRepository;
import com.library.libraryservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LibraryServiceApplication.class)
@AutoConfigureMockMvc
public class RentalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @BeforeEach
    public void setup() {
        rentalRepository.deleteAll();
        userRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    public void testRentBook() throws Exception {
        Users user = new Users(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, LocalDateTime.now(), null);
        user = userRepository.save(user);
        Books book = new Books(null, "Book One", "Category One", 10, true, null);
        book = bookRepository.save(book);

        MvcResult result = mockMvc.perform(post("/rentals/rent")
                        .param("userId", user.getId().toString())
                        .param("bookId", book.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println(jsonResponse);
                //.andExpect(jsonPath("$.user.id").value(1L));
                //.andExpect(jsonPath("$.book.id").value(book.getId()));
    }

    @Test
    public void testReturnBook() throws Exception {
        Users user = new Users(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, LocalDateTime.now(), null);
        user = userRepository.save(user);
        Books book = new Books(null, "Book One", "Category One", 10, true, null);
        book = bookRepository.save(book);
        Rentals rental = new Rentals(null, user, book, LocalDateTime.now(), null);
        rental = rentalRepository.save(rental);

        mockMvc.perform(post("/rentals/return/{id}", rental.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rental.getId()))
                .andExpect(jsonPath("$.returnDate").isNotEmpty())
                .andExpect(jsonPath("$.book.available").value(true));
    }
}
