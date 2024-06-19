package com.library.libraryservice.service;

import com.library.libraryservice.dto.UsersDTO;
import com.library.libraryservice.dto.BooksDTO;
import com.library.libraryservice.dto.RentalsDTO;
import com.library.libraryservice.enums.UserProfile;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.model.Books;
import com.library.libraryservice.model.Rentals;
import com.library.libraryservice.model.Users;
import com.library.libraryservice.repository.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private RentalService rentalService;

    @Test
    public void testRentBook() {
        UsersDTO userDTO = new UsersDTO(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        BooksDTO bookDTO = new BooksDTO(1L, "Book One", "Category One", 10, true, null);
        RentalsDTO rentalDTO = new RentalsDTO(1L, userDTO, bookDTO, null, null);

        when(userService.getUserById(anyLong())).thenReturn(userDTO);
        when(bookService.getBookById(anyLong())).thenReturn(bookDTO);
        when(rentalRepository.save(any(Rentals.class))).thenReturn(rentalDTO.toEntity());

        RentalsDTO result = rentalService.rentBook(1L, 1L);
        assertEquals(rentalDTO, result);
    }

    @Test
    public void testReturnBook() {
        UsersDTO userDTO = new UsersDTO(1L, "John Doe", "john.doe@example.com", UserProfile.USER, true, null, null);
        BooksDTO bookDTO = new BooksDTO(1L, "Book One", "Category One", 10, true, null);
        RentalsDTO rentalDTO = new RentalsDTO(1L, userDTO, bookDTO, null, null);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rentalDTO.toEntity()));
        when(rentalRepository.save(any(Rentals.class))).thenReturn(rentalDTO.toEntity());
        when(bookService.saveBook(any(BooksDTO.class))).thenReturn(bookDTO);

        RentalsDTO result = rentalService.returnBook(1L);
        assertEquals(rentalDTO, result);
    }

    @Test
    public void testReturnBook_NotFound() {
        when(rentalRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rentalService.returnBook(1L));
    }
}
