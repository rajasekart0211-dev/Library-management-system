package com.example.library_management_system.service.impl;

import com.example.library_management_system.Exception.BookException;
import com.example.library_management_system.mapper.BookMapper;
import com.example.library_management_system.model.Book;
import com.example.library_management_system.payload.dto.BookDTO;
import com.example.library_management_system.payload.request.BookSearchRequest;
import com.example.library_management_system.payload.response.PageResponse;
import com.example.library_management_system.repository.BookRepository;
import com.example.library_management_system.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDTO> getAllBooks(){
        return bookRepository.findAll().stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    @Override
    public BookDTO createBook(BookDTO dto) throws BookException {

        if(bookRepository.existsByIsbn(dto.getIsbn())){
            throw new BookException("Book with isbn: "+dto.getIsbn()+" already exists!");
        }
        Book book = bookMapper.toEntity(dto);
        book.isAvailable();
        return bookMapper.toDTO(bookRepository.save(book));
    }

    @Override
    public BookDTO getBookById(Long id) throws BookException {
        return bookMapper.toDTO(bookRepository.findById(id).orElseThrow(
                ()-> new BookException("Book not found")
        ));
    }

    @Override
    public List<BookDTO> createBooksInBulk(List<BookDTO> dtos) throws BookException {

        List<BookDTO> createdBooks = new ArrayList<>();
        for(BookDTO dto : dtos){
            createdBooks.add(createBook(dto));
        }
        return createdBooks;
    }

    @Override
    public BookDTO getBookByIsbn(String isbn) throws BookException {
        return bookMapper.toDTO(bookRepository.findByIsbn(isbn).orElseThrow(
                ()-> new BookException("Book not found with isbn:"+isbn)
        ));
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) throws BookException {

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(()-> new BookException("Book not found"));

        bookMapper.updateEntityFromDto(bookDTO, existingBook);
        existingBook.isAvailable();
        return bookMapper.toDTO(bookRepository.save(existingBook));
    }

    @Override
    public void deleteBook(Long id) throws BookException {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(()-> new BookException("Book not found"));

        existingBook.setActive(false);
        bookRepository.save(existingBook);
    }

    @Override
    public void hardDelete(Long id) throws BookException {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(()-> new BookException("Book with id:"+id+"doesn't exist"));

        bookRepository.delete(existingBook);
    }

    @Override
    public PageResponse<BookDTO> searchBookWithFilters(BookSearchRequest searchRequest) {

        Pageable pageable = createPageable(searchRequest.getPage(),
                searchRequest.getSize(), searchRequest.getSortBy(),
                searchRequest.getSortDirection());

        Page<Book> bookPage = bookRepository.searchBookWithFilters(
                searchRequest.getSearchTerm(),
                searchRequest.getGenreId(),
                searchRequest.getAvailableOnly(),
                pageable
        );

        return convertToPageResponse(bookPage);
    }

    @Override
    public long getTotalActiveBooks() {
        return bookRepository.countByActiveTrue();
    }

    @Override
    public long getTotalAvailableBooks() {
        return bookRepository.countAvailableBooks();
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection){
        size = Math.min(size, 10);
        size=Math.max(size, 1);

        Sort sort = sortDirection.equalsIgnoreCase("ASC")?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }

    private PageResponse<BookDTO> convertToPageResponse(Page<Book> books){
        List<BookDTO> bookDTOS = books.getContent()
                .stream()
                .map(bookMapper::toDTO)
                .toList();

        return new PageResponse<>(bookDTOS,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast(),
                books.isFirst(),
                books.isEmpty()
        );
    }

}
