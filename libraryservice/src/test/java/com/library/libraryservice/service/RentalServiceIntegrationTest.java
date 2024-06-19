package com.library.libraryservice.service;

import com.library.libraryservice.enums.UserProfile;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.dto.BooksDTO;
import com.library.libraryservice.dto.RentalsDTO;
import com.library.libraryservice.dto.UsersDTO;
import com.library.libraryservice.model.Books;
import com.library.libraryservice.model.Rentals;
import com.library.libraryservice.model.Users;
import com.library.libraryservice.repository.BookRepository;
import com.library.libraryservice.repository.RentalRepository;
import com.library.libraryservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RentalServiceIntegrationTest {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        rentalRepository.deleteAll();
        userRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    public void testRentBook() {
        Users user = new Users(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        user = userRepository.save(user);
        Books book = new Books(null, "Book One", "Category One", 10, true, null);
        book = bookRepository.save(book);

        UsersDTO userDTO = UsersDTO.fromEntity(user);
        BooksDTO bookDTO = BooksDTO.fromEntity(book);

        RentalsDTO rentalDTO = rentalService.rentBook(userDTO.getId(), bookDTO.getId());
        assertNotNull(rentalDTO.getId());
        assertEquals(userDTO.getId(), rentalDTO.getUser().getId());
        assertEquals(bookDTO.getId(), rentalDTO.getBook().getId());
    }

    @Test
    public void testReturnBook() {
        Users user = new Users(null, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        user = userRepository.save(user);
        Books book = new Books(null, "Book One", "Category One", 10, true, null);
        book = bookRepository.save(book);
        Rentals rental = new Rentals(null, user, book, null, null);
        rental = rentalRepository.save(rental);

        RentalsDTO returnedRentalDTO = rentalService.returnBook(rental.getId());
        assertNotNull(returnedRentalDTO.getReturnDate());
        BooksDTO returnedBookDTO = returnedRentalDTO.getBook();
        assertTrue(returnedBookDTO.isAvailable());
    }

    @Test
    public void testReturnBook_NotFound() {
        assertThrows(NotFoundException.class, () -> rentalService.returnBook(1L));
    }
}
