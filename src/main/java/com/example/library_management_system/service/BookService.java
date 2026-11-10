package com.example.library_management_system.service;

import com.example.library_management_system.Exception.BookException;
import com.example.library_management_system.payload.dto.BookDTO;
import com.example.library_management_system.payload.request.BookSearchRequest;
import com.example.library_management_system.payload.response.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {

    List<BookDTO> getAllBooks();

    BookDTO createBook(BookDTO bookDTO) throws BookException;

    BookDTO getBookById(Long id) throws BookException;

    List<BookDTO> createBooksInBulk(List<BookDTO> dtos) throws BookException;

    BookDTO getBookByIsbn(String isbn) throws BookException;

    BookDTO updateBook(Long id, BookDTO bookDTO) throws BookException;

    void deleteBook(Long id) throws BookException;

    void hardDelete(Long id) throws BookException;

    PageResponse<BookDTO> searchBookWithFilters(
            BookSearchRequest searchRequest
    );

    long getTotalActiveBooks();

    long getTotalAvailableBooks();
}
