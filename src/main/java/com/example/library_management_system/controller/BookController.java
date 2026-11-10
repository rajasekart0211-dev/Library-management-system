package com.example.library_management_system.controller;

import com.example.library_management_system.Exception.BookException;
import com.example.library_management_system.payload.dto.BookDTO;
import com.example.library_management_system.payload.request.BookSearchRequest;
import com.example.library_management_system.payload.response.ApiResponse;
import com.example.library_management_system.payload.response.PageResponse;
import com.example.library_management_system.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(){
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO dto) throws BookException {
        BookDTO createdBook = bookService.createBook(dto);
        return ResponseEntity.ok().body(createdBook);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<BookDTO>> createBooksInBulk(@Valid @RequestBody List<BookDTO> dtos) throws BookException {
        List<BookDTO> createdBooks = bookService.createBooksInBulk(dtos);
        return ResponseEntity.ok().body(createdBooks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) throws BookException {
        BookDTO bookDTO = bookService.getBookById(id);
        return ResponseEntity.ok().body(bookDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id,
                                              @RequestBody BookDTO dto) throws BookException {
            BookDTO bookDTO = bookService.updateBook(id, dto);
            return ResponseEntity.ok().body(bookDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) throws BookException {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new ApiResponse("Book deleted Successfully",true));
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ApiResponse> hardDelete(@PathVariable Long id) throws BookException {
        bookService.hardDelete(id);
        return ResponseEntity.ok(new ApiResponse("Book deleted successfully(HARD)",true));
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<BookDTO>> advancedSearch(
            @RequestBody BookSearchRequest searchRequest){
        PageResponse<BookDTO> books = bookService.searchBookWithFilters(searchRequest);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/stats")
    public ResponseEntity<BookStatsResponse> getBookStats(){
        long totalActive = bookService.getTotalActiveBooks();
        long totalAvailable = bookService.getTotalAvailableBooks();

        return ResponseEntity.ok(new BookStatsResponse(totalActive, totalAvailable));
    }

    @GetMapping("/search-new")
    public ResponseEntity<PageResponse<BookDTO>> searchBooks(
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false, defaultValue = "true") Boolean availableOnly,
            @RequestParam(defaultValue = "true") boolean activeOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ){
        BookSearchRequest searchRequest = new BookSearchRequest();
        searchRequest.setGenreId(genreId);
        searchRequest.setAvailableOnly(availableOnly);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);

        PageResponse<BookDTO> books = bookService.searchBookWithFilters(searchRequest);
        return ResponseEntity.ok(books);
    }

    public static class BookStatsResponse{
        public long totalActiveBooks;
        public long totalAvailableBooks;

        public BookStatsResponse(long totalActiveBooks, long totalAvailableBooks){
            this.totalActiveBooks = totalActiveBooks;
            this.totalAvailableBooks = totalAvailableBooks;
        }
    }

}
