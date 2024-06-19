package com.library.libraryservice.dto;

import com.library.libraryservice.model.Rentals;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentalsDTO {
    private Long id;
    private UsersDTO user;
    private BooksDTO book;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;

    public RentalsDTO() {
    }

    public RentalsDTO(Long id, UsersDTO user, BooksDTO book, LocalDateTime rentalDate, LocalDateTime returnDate) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.rentalDate = rentalDate != null ? rentalDate : LocalDateTime.now();
        this.returnDate = returnDate;
    }

    public static RentalsDTO fromEntity(Rentals rental) {
        RentalsDTO dto = new RentalsDTO();
        dto.setId(rental.getId());
        dto.setUser(UsersDTO.fromEntity(rental.getUser()));
        dto.setBook(BooksDTO.fromEntity(rental.getBook()));
        return dto;
    }

    public Rentals toEntity() {
        Rentals rental = new Rentals();
        rental.setId(this.id);
        rental.setUser(this.user.toEntity());
        rental.setBook(this.book.toEntity());
        return rental;
    }
}
