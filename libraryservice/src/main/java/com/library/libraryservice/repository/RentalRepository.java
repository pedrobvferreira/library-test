package com.library.libraryservice.repository;

import com.library.libraryservice.model.Rentals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rentals, Long> {
}
