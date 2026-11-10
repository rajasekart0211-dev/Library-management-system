package com.example.library_management_system.mapper;

import com.example.library_management_system.Exception.BookException;
import com.example.library_management_system.model.Book;
import com.example.library_management_system.model.Genre;
import com.example.library_management_system.payload.dto.BookDTO;
import com.example.library_management_system.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final GenreRepository genreRepository;

    public BookDTO toDTO(Book book){
        if(book == null)return null;

        BookDTO bookDTO = BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .genreName(book.getGenre().getName())
                .genreCode(book.getGenre().getCode())
                .genreId(book.getGenre().getId())
                .isbn(book.getIsbn())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .publicationDate(book.getPublicationDate())
                .language(book.getLanguage())
                .pages(book.getPages())
                .description(book.getDescription())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .price(book.getPrice())
                .coverImageUrl(book.getCoverImageUrl())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();

        if(book.getActive() != null){
            bookDTO.setActive(book.getActive());
        }

        return bookDTO;
    }

    public Book toEntity(BookDTO dto) throws BookException{
        if(dto == null)return null;

        Book book = Book.builder()
                .id(dto.getId())
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .publicationDate(dto.getPublicationDate())
                .language(dto.getLanguage())
                .pages(dto.getPages())
                .description(dto.getDescription())
                .totalCopies(dto.getTotalCopies())
                .availableCopies(dto.getAvailableCopies())
                .price(dto.getPrice())
                .coverImageUrl(dto.getCoverImageUrl())
                .build();

        if(dto.getActive() != null){
            book.setActive(dto.getActive());
        }

        if(dto.getGenreId() != null){
            Genre genre = genreRepository.findById(dto.getGenreId()).orElseThrow(
                    ()-> new BookException("Genre with id: "+dto.getGenreId()+" doesn't exist!")
            );

            book.setGenre(genre);
        }

        return book;
    }

    public Book updateEntityFromDto(BookDTO dto, Book book) throws BookException {
        if(book == null || dto == null) return null;

        book.setAuthor(dto.getAuthor());
        book.setTitle(dto.getTitle());
        book.setPublisher(dto.getPublisher());
        book.setPublicationDate(dto.getPublicationDate());
        book.setLanguage(dto.getLanguage());
        book.setPages(dto.getPages());
        book.setDescription(dto.getDescription());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(dto.getAvailableCopies());
        book.setPrice(dto.getPrice());
        book.setCoverImageUrl(dto.getCoverImageUrl());


        if(dto.getGenreId() != null){
            Genre genre = genreRepository.findById(dto.getGenreId())
                    .orElseThrow(()-> new BookException("Genre with id: "+dto.getId()+" doesn't exist!")
                    );

            book.setGenre(genre);
        }

        return book;
    }

}
