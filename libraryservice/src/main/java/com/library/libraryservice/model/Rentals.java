package com.library.libraryservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Rentals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Books book;

    @Column(name = "rental_date")
    private LocalDateTime rentalDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    public Rentals() {
    }

    public Rentals(Long id, Users user, Books book, LocalDateTime rentalDate, LocalDateTime returnDate) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.rentalDate = rentalDate != null ? rentalDate : LocalDateTime.now();
        this.returnDate = returnDate;
    }

    public Rentals returnBook() {
        return new Rentals(this.id, this.user, this.book, this.rentalDate, LocalDateTime.now());
    }
}
