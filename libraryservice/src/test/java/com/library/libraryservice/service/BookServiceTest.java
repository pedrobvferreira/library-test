package com.library.libraryservice.service;

import com.library.libraryservice.dto.BooksDTO;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.model.Books;
import com.library.libraryservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void testGetAllBooks() {
        Books book1 = new Books(1L, "Book One", "Category One", 10, true, null);
        Books book2 = new Books(2L, "Book Two", "Category Two", 5, true, null);
        List<Books> books = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<BooksDTO> result = bookService.getAllBooks();
        assertEquals(2, result.size());
        assertEquals("Book One", result.get(0).getTitle());
    }

    @Test
    public void testGetBookById() {
        Books book = new Books(1L, "Book One", "Category One", 10, true, null);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BooksDTO result = bookService.getBookById(1L);
        assertEquals(BooksDTO.fromEntity(book), result);
    }

    @Test
    public void testGetBookById_NotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    public void testSaveBook() {
        Books book = new Books(1L, "Book One", "Category One", 10, true, null);
        BooksDTO bookDTO = BooksDTO.fromEntity(book);
        when(bookRepository.save(any(Books.class))).thenReturn(book);

        BooksDTO result = bookService.saveBook(bookDTO);
        assertEquals(bookDTO, result);
    }

    @Test
    public void testUpdateBook() {
        Books existingBook = new Books(1L, "Book One", "Category One", 10, true, null);
        Books updatedBook = new Books(1L, "Book Two", "Category Two", 5, true, null);
        BooksDTO updatedBookDTO = BooksDTO.fromEntity(updatedBook);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Books.class))).thenReturn(updatedBook);

        BooksDTO result = bookService.updateBook(1L, updatedBookDTO);
        assertEquals(updatedBookDTO, result);
    }

    @Test
    public void testDeleteBook() {
        Books book = new Books(1L, "Book One", "Category One", 10, true, null);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        ArgumentCaptor<Books> bookCaptor = ArgumentCaptor.forClass(Books.class);
        verify(bookRepository).delete(bookCaptor.capture());
        Books deletedBook = bookCaptor.getValue();

        assertEquals(1L, deletedBook.getId());
    }
}
