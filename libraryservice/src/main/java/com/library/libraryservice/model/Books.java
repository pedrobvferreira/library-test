package com.library.libraryservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Entity
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String category;

    private int quantity;

    private boolean available;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rentals> rentals;

    public Books() {
    }

    public Books(Long id, String title, String category, int quantity, boolean available, List<Rentals> rentals) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.quantity = quantity;
        this.available = available;
        this.rentals = rentals;
    }

    public Books markAsUnavailable() {
        return new Books(this.id, this.title, this.category, this.quantity, false, this.rentals);
    }

    public Books markAsAvailable() {
        return new Books(this.id, this.title, this.category, this.quantity, true, this.rentals);
    }
}
