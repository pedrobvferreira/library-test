package com.library.libraryservice.service;

import com.library.libraryservice.dto.BooksDTO;
import com.library.libraryservice.dto.RentalsDTO;
import com.library.libraryservice.exception.NotFoundException;
import com.library.libraryservice.model.Books;
import com.library.libraryservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<BooksDTO> getAllBooks() {
        List<Books> books = bookRepository.findAll();
        return books.stream().map(BooksDTO::fromEntity).collect(Collectors.toList());
    }

    public BooksDTO getBookById(Long id) {
        Books book = bookRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return BooksDTO.fromEntity(book);
    }

    public BooksDTO saveBook(BooksDTO bookDTO) {
        Books book = bookDTO.toEntity();
        book = bookRepository.save(book);
        return BooksDTO.fromEntity(book);
    }

    public void deleteBook(Long id) {
        BooksDTO bookDTO = getBookById(id);
        Books book = bookDTO.toEntity();
        bookRepository.delete(book);
    }

    public BooksDTO updateBook(Long id, BooksDTO updatedBookDTO) {
        return bookRepository.findById(id).map(existingBook -> {
            existingBook.setTitle(updatedBookDTO.getTitle());
            existingBook.setCategory(updatedBookDTO.getCategory());
            existingBook.setQuantity(updatedBookDTO.getQuantity());
            existingBook.setAvailable(updatedBookDTO.isAvailable());
            existingBook.setRentals(updatedBookDTO.getRentals().stream().map(RentalsDTO::toEntity).collect(Collectors.toList()));
            Books updatedBook = bookRepository.save(existingBook);
            return BooksDTO.fromEntity(updatedBook);
        }).orElseThrow(NotFoundException::new);
    }
}
