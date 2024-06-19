package com.library.libraryservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.libraryservice.enums.UserProfile;
import com.library.libraryservice.model.Rentals;
import com.library.libraryservice.model.Users;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UsersDTO {
    private Long id;
    private String name;
    private String email;
    private UserProfile profile;
    private boolean active;
    private LocalDateTime createdDate;
    @JsonIgnore
    private List<RentalsDTO> rentals;

    public UsersDTO() {
    }

    public UsersDTO(Long id, String name, String email, UserProfile profile, boolean active, LocalDateTime createdDate, List<RentalsDTO> rentals) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.active = active;
        this.createdDate = createdDate != null ? createdDate : LocalDateTime.now(ZoneId.systemDefault());
        this.rentals = rentals != null ? rentals : new ArrayList<>();
    }

    public static UsersDTO fromEntity(Users user) {
        UsersDTO dto = new UsersDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setProfile(user.getProfile());
        dto.setActive(user.isActive());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setRentals(user.getRentals().stream().map(RentalsDTO::fromEntity).collect(Collectors.toList()));
        return dto;
    }

    public Users toEntity() {
        Users user = new Users();
        user.setId(this.id);
        user.setName(this.name);
        user.setEmail(this.email);
        user.setRentals(this.rentals.stream().map(RentalsDTO::toEntity).collect(Collectors.toList()));
        return user;
    }
}
