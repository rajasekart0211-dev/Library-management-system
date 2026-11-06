package com.example.library_management_system.mapper;

import com.example.library_management_system.payload.dto.GenreDTO;
import com.example.library_management_system.model.Genre;
import com.example.library_management_system.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreMapper {

    private final GenreRepository genreRepository;

    public List<GenreDTO> toDTOList(List<Genre> genreList){
        return genreList.stream().map(this::toDto).toList();
    }

    public void updateEntityFromDto(Genre genre, GenreDTO genreDto){
        if(genreDto == null || genre == null) return;

        genre.setCode(genreDto.getCode());
        genre.setName(genreDto.getName());
        genre.setDescription(genreDto.getDescription());
        genre.setDisplayOrder(genreDto.getDisplayOrder());
        genre.setActive(genreDto.isActive());

        if(genreDto.getParentGenreId() != null){
            genreRepository.findById(genreDto.getParentGenreId())
                    .ifPresent(genre::setParentGenre);
        }

    }

    public GenreDTO toDto(Genre genre){

            if(genre == null) return null;

            GenreDTO savedDto = GenreDTO.builder()
                    .id(genre.getId())
                    .code(genre.getCode())
                    .name(genre.getName())
                    .description(genre.getDescription())
                    .displayOrder(genre.getDisplayOrder())
                    .active(genre.isActive())
                    .createdAt(genre.getCreatedAt())
                    .updatedAt(genre.getUpdatedAt())
                    .build();

            if(genre.getParentGenre() != null){
                savedDto.setParentGenreId(genre.getParentGenre().getId());
                savedDto.setParentGenreName(genre.getParentGenre().getName());
            }

            if(genre.getSubGenre() != null && !genre.getSubGenre().isEmpty()) {
                savedDto.setSubGenre(genre.getSubGenre().stream()
                        .filter(Genre::isActive)
                        .map(this::toDto)
                        .toList());
            }

            return savedDto;
    }

    public Genre toModel(GenreDTO dto){

            if(dto == null) return null;

            Genre genre = Genre.builder()
                    .code(dto.getCode())
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .displayOrder(dto.getDisplayOrder())
                    .active(dto.isActive())
                    .build();

            if(dto.getParentGenreId() != null){
                genreRepository.findById(dto.getParentGenreId())
                        .ifPresent(genre::setParentGenre);
            }
            return genre;
    }
}
