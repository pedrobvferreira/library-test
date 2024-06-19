package com.library.libraryservice.controller;

import com.library.libraryservice.dto.UsersDTO;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.model.Users;
import com.library.libraryservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "View a list of available users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<List<UsersDTO>> getAllUsers() {
        List<UsersDTO> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @Operation(summary = "Get a user by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUserById(@PathVariable Long id) {
        try {
            UsersDTO u = userService.getUserById(id);
            return new ResponseEntity<>(u, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<UsersDTO> createUser(@RequestBody UsersDTO userDTO) {
        UsersDTO createdUser = userService.saveUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsersDTO> updateUser(@PathVariable Long id, @RequestBody UsersDTO userDTO) {
        UsersDTO u = userService.updateUser(id, userDTO);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @Operation(summary = "Deactivate a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
