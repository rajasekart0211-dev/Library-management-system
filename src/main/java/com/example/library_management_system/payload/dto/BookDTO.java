package com.example.library_management_system.payload.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

    private Long id;

    private String language;

    @NotBlank(message = "ISBN is mandatory")
    private String isbn;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 255, message = "Title must be between 1-255 characters")
    private String title;

    @Size(min = 1, max = 255, message = "Author must be between 1-255 characters")
    private String author;

    @NotNull(message = "Genre id is mandatory")
    private Long genreId;

    private String genreName;

    private String genreCode;

    @Size(min = 1, max = 100, message = "Publisher must be between 1-100 characters")
    private String publisher;

    private LocalDate publicationDate;

    @Min(value = 1, message = "Pages must be atleast 1")
    @Max(value = 50000, message = "Pages must not exceed 50,000")
    private Integer pages;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Min(value = 0, message = "Total copies must not be negative")
    @NotNull(message = "Total copies is mandatory")
    private Integer totalCopies;

    @Min(value = 0, message = "Available copies must not be negative")
    @NotNull(message = "Available copies is mandatory")
    private Integer availableCopies;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "prices must have at most 8 integer digits and 2 decimal digits")
    private BigDecimal price;

    @Size(max = 500, message = "image url must not exceed 500 characters")
    private String coverImageUrl;

    private Boolean alreadyHaveLoan;

    private Boolean alreadyHaveReservation;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
