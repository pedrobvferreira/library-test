package com.library.libraryservice.controller;

import com.library.libraryservice.dto.RentalsDTO;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @Operation(summary = "View a list of available rental")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<List<RentalsDTO>> getAllRentals() {
        List<RentalsDTO> userList = rentalService.getAllRentals();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @Operation(summary = "Get a rental by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rental"),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RentalsDTO> getRentalById(@PathVariable Long id) {
        try {
            RentalsDTO rental = rentalService.getRentalById(id);
            return ResponseEntity.ok(rental);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Rent a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rental created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/rent")
    public ResponseEntity<RentalsDTO> rentBook(@RequestParam Long userId, @RequestParam Long bookId) {
        try {
            RentalsDTO r = rentalService.rentBook(userId, bookId);
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Return a rental")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental returned successfully"),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @PostMapping("/return/{id}")
    public ResponseEntity<RentalsDTO> returnBook(@PathVariable Long id) {
        try {
            RentalsDTO r = rentalService.returnBook(id);
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
