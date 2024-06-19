package com.library.libraryservice.service;

import com.library.libraryservice.dto.BooksDTO;
import com.library.libraryservice.dto.RentalsDTO;
import com.library.libraryservice.dto.UsersDTO;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.model.Books;
import com.library.libraryservice.model.Rentals;
import com.library.libraryservice.model.Users;
import com.library.libraryservice.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    public List<RentalsDTO> getAllRentals() {
        List<Rentals> rentals = rentalRepository.findAll();
        return rentals.stream().map(RentalsDTO::fromEntity).collect(Collectors.toList());
    }

    public RentalsDTO getRentalById(Long id) {
        Rentals rental = rentalRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return RentalsDTO.fromEntity(rental);
    }

    public RentalsDTO rentBook(Long userId, Long bookId) {
        UsersDTO userDTO = userService.getUserById(userId);
        BooksDTO bookDTO = bookService.getBookById(bookId);

        if (userDTO == null || bookDTO == null || !bookDTO.isAvailable()) {
            throw new NotFoundException();
        }

        Users user = userDTO.toEntity();
        Books book = bookDTO.toEntity();

        Rentals rental = new Rentals(null, user, book, LocalDateTime.now(), null);
        Books updatedBook = book.markAsUnavailable();

        bookService.saveBook(BooksDTO.fromEntity(updatedBook));
        Rentals savedRental = rentalRepository.save(rental);    // Save rental as entity

        return RentalsDTO.fromEntity(savedRental);
    }

    public RentalsDTO returnBook(Long rentalId) {
        return rentalRepository.findById(rentalId).map(rental -> {
            if (rental.getReturnDate() == null) {
                Rentals updatedRental = rental.returnBook();
                Books book = updatedRental.getBook().markAsAvailable();

                bookService.saveBook(BooksDTO.fromEntity(book));
                Rentals savedUpdatedRental = rentalRepository.save(updatedRental);  // Save rental as entity

                return RentalsDTO.fromEntity(savedUpdatedRental);
            } else {
                return RentalsDTO.fromEntity(rental);
            }
        }).orElseThrow(NotFoundException::new);
    }
}
