package com.library.libraryservice.controller;

import com.library.libraryservice.dto.BooksDTO;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.model.Books;
import com.library.libraryservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Operation(summary = "View a list of available books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<List<BooksDTO>> getAllBooks() {
        List<BooksDTO> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Operation(summary = "Get a book by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved book"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BooksDTO> getBookById(@PathVariable Long id) {
        try {
            BooksDTO book = bookService.getBookById(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<BooksDTO> createBook(@RequestBody BooksDTO bookDTO) {
        BooksDTO createdBook = bookService.saveBook(bookDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BooksDTO> updateBook(@PathVariable Long id, @RequestBody BooksDTO bookDTO) {
        BooksDTO b = bookService.updateBook(id, bookDTO);
        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @Operation(summary = "Delete a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
