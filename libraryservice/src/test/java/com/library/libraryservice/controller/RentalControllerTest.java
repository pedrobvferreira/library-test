package com.library.libraryservice.controller;

import com.library.libraryservice.dto.RentalsDTO;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.service.RentalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RentalControllerTest {

    @Mock
    private RentalService rentalService;

    @InjectMocks
    private RentalController rentalController;

    @Test
    public void testRentBook() {
        RentalsDTO rentalDTO = new RentalsDTO(1L, null, null, null, null);
        when(rentalService.rentBook(anyLong(), anyLong())).thenReturn(rentalDTO);

        ResponseEntity<RentalsDTO> response = rentalController.rentBook(1L, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rentalDTO, response.getBody());
    }

    @Test
    public void testRentBook_InvalidInput() {
        when(rentalService.rentBook(anyLong(), anyLong())).thenThrow(new IllegalArgumentException());

        ResponseEntity<RentalsDTO> response = rentalController.rentBook(1L, 1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testReturnBook() {
        RentalsDTO rentalDTO = new RentalsDTO(1L, null, null, null, null);
        when(rentalService.returnBook(anyLong())).thenReturn(rentalDTO);

        ResponseEntity<RentalsDTO> response = rentalController.returnBook(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rentalDTO, response.getBody());
    }

    @Test
    public void testReturnBook_NotFound() {
        when(rentalService.returnBook(anyLong())).thenThrow(new NotFoundException());

        ResponseEntity<RentalsDTO> response = rentalController.returnBook(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}