package com.library.libraryservice.repository;

import com.library.libraryservice.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {
}
