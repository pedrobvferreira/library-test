package com.library.libraryservice.controller;

import com.library.libraryservice.dto.BooksDTO;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    public void testGetAllBooks() {
        BooksDTO book1 = new BooksDTO(1L, "Book One", "Category One", 10, true, null);
        BooksDTO book2 = new BooksDTO(2L, "Book Two", "Category Two", 5, true, null);
        List<BooksDTO> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<BooksDTO>> response = bookController.getAllBooks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BooksDTO> result = response.getBody();
        assert result != null;
        assertEquals(2, result.size());
        assertEquals("Book One", result.get(0).getTitle());
    }

    @Test
    public void testGetBookById() {
        BooksDTO book = new BooksDTO(1L, "Book One", "Category One", 10, true, null);
        when(bookService.getBookById(1L)).thenReturn(book);

        ResponseEntity<BooksDTO> response = bookController.getBookById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    public void testGetBookById_NotFound() {
        when(bookService.getBookById(anyLong())).thenThrow(new NotFoundException());

        ResponseEntity<BooksDTO> response = bookController.getBookById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateBook() {
        BooksDTO book = new BooksDTO(1L, "Book One", "Category One", 10, true, null);
        when(bookService.saveBook(any(BooksDTO.class))).thenReturn(book);

        ResponseEntity<BooksDTO> response = bookController.createBook(book);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    public void testUpdateBook() {
        BooksDTO updatedBook = new BooksDTO(1L, "Book One", "Category One", 10, true, null);
        when(bookService.updateBook(anyLong(), any(BooksDTO.class))).thenReturn(updatedBook);

        ResponseEntity<BooksDTO> response = bookController.updateBook(1L, updatedBook);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBook, response.getBody());
    }

    @Test
    public void testDeleteBook() {
        doNothing().when(bookService).deleteBook(anyLong());

        ResponseEntity<Void> response = bookController.deleteBook(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService, times(1)).deleteBook(1L);
    }
}
