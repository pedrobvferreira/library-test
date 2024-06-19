package com.library.libraryservice.service;

import com.library.libraryservice.dto.BooksDTO;
import com.library.libraryservice.model.Books;
import com.library.libraryservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        bookRepository.deleteAll();
    }

    @Test
    public void testGetAllBooks() {
        Books book1 = new Books(null, "Book One", "Category One", 10, true, null);
        Books book2 = new Books(null, "Book Two", "Category Two", 5, true, null);
        bookRepository.saveAll(List.of(book1, book2));

        List<BooksDTO> books = bookService.getAllBooks();
        assertEquals(2, books.size());
        assertEquals("Book One", books.get(0).getTitle());
        assertEquals("Book Two", books.get(1).getTitle());
    }

    @Test
    public void testSaveBook() {
        BooksDTO bookDTO = new BooksDTO(null, "Book One", "Category One", 10, true, null);
        BooksDTO savedBookDTO = bookService.saveBook(bookDTO);
        assertNotNull(savedBookDTO.getId());
        assertEquals("Book One", savedBookDTO.getTitle());
    }

    @Test
    public void testGetBookById() {
        Books book = new Books(null, "Book One", "Category One", 10, true, null);
        book = bookRepository.save(book);

        BooksDTO foundBookDTO = bookService.getBookById(book.getId());
        assertEquals(book.getId(), foundBookDTO.getId());
    }

    @Test
    public void testUpdateBook() {
        Books book = new Books(null, "Book One", "Category One", 10, true, null);
        book = bookRepository.save(book);

        BooksDTO updatedBookDTO = new BooksDTO(book.getId(), "Book Two", "Category Two", 5, true, null);
        BooksDTO result = bookService.updateBook(book.getId(), updatedBookDTO);

        assertEquals("Book Two", result.getTitle());
        assertEquals("Category Two", result.getCategory());
    }

    @Test
    public void testDeleteBook() {
        Books book = new Books(null, "Book One", "Category One", 10, true, null);
        book = bookRepository.save(book);

        bookService.deleteBook(book.getId());
        assertFalse(bookRepository.findById(book.getId()).isPresent());
    }
}
