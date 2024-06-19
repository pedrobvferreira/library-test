package com.library.libraryservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.libraryservice.enums.UserProfile;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserProfile profile;

    private boolean active;

    @Column(name = "created_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rentals> rentals;

    public Users() {
    }

    public Users(Long id, String name, String email, UserProfile profile, boolean active, LocalDateTime createdDate, List<Rentals> rentals) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.active = active;
        this.createdDate = createdDate != null ? createdDate : LocalDateTime.now(ZoneId.systemDefault());
        this.rentals = rentals;
    }

    public Users deactivate() {
        return new Users(this.id, this.name, this.email, this.profile, false, this.createdDate, this.rentals);
    }
}
