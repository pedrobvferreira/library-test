package com.library.libraryservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.libraryservice.model.Books;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BooksDTO {
    private Long id;
    private String title;
    private String category;
    private int quantity;
    private boolean available;
    @JsonIgnore
    private List<RentalsDTO> rentals;

    public BooksDTO() {
    }

    public BooksDTO(Long id, String title, String category, int quantity, boolean available, List<RentalsDTO> rentals) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.quantity = quantity;
        this.available = available;
        this.rentals = rentals != null ? rentals : new ArrayList<>();
    }

    public static BooksDTO fromEntity(Books book) {
        BooksDTO dto = new BooksDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setCategory(book.getCategory());
        dto.setQuantity(book.getQuantity());
        dto.setAvailable(book.isAvailable());
        dto.setRentals(book.getRentals().stream().map(RentalsDTO::fromEntity).collect(Collectors.toList()));
        return dto;
    }

    public Books toEntity() {
        Books book = new Books();
        book.setId(this.id);
        book.setTitle(this.title);
        book.setCategory(this.getCategory());
        book.setQuantity(this.getQuantity());
        book.setAvailable(this.isAvailable());
        book.setRentals(this.rentals.stream().map(RentalsDTO::toEntity).collect(Collectors.toList()));
        return book;
    }
}
