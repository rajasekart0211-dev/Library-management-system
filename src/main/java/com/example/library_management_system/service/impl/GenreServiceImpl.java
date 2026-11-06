package com.example.library_management_system.service.impl;

import com.example.library_management_system.Exception.GenreException;
import com.example.library_management_system.payload.dto.GenreDTO;
import com.example.library_management_system.mapper.GenreMapper;
import com.example.library_management_system.model.Genre;
import com.example.library_management_system.repository.GenreRepository;
import com.example.library_management_system.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public GenreDTO createGenre(GenreDTO genreDto) {
        Genre savedGenre = genreMapper.toModel(genreDto);

        return genreMapper.toDto(genreRepository.save(savedGenre));
    }

    @Override
    public List<GenreDTO> getAllGenres(){
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @Override
    public GenreDTO getGenreById(Long genreId) throws GenreException {
        Genre foundGenre = genreRepository.findById(genreId).orElseThrow(
                ()-> new GenreException("Genre with id:"+ genreId+" not found")
        );
        return genreMapper.toDto(foundGenre);
    }

    @Override
    public GenreDTO updateGenre(Long genreId, GenreDTO genreDto) throws GenreException {
        Genre existingGenre = genreRepository.findById(genreId)
                .orElseThrow(()-> new GenreException("Genre Not found"));
        genreMapper.updateEntityFromDto(existingGenre, genreDto);
        genreRepository.save(existingGenre);
        return genreMapper.toDto(existingGenre);
    }

    @Override
    public void deleteGenreById(Long genreId) throws GenreException {
        Genre existingGenre = genreRepository.findById(genreId)
                .orElseThrow(()-> new GenreException("Genre Not found"));
        existingGenre.setActive(false);
        genreRepository.deleteById(genreId);
    }

    @Override
    public void hardDeleteGenre(Long genreId) throws GenreException {
        Genre existingGenre = genreRepository.findById(genreId)
                .orElseThrow(()-> new GenreException("Genre Not found"));

        genreRepository.deleteById(genreId);
    }

    @Override
    public List<GenreDTO> getAllActiveGenresWithSubGenres() {
        return genreMapper.toDTOList(genreRepository
                .findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc());
    }

    @Override
    public List<GenreDTO> getTopLevelGenres() {
        return genreMapper.toDTOList(genreRepository
                .findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc());
    }

    @Override
    public long getTotalActiveGenres() {
        return genreRepository.countByActiveTrue();
    }

    @Override
    public long getBookCountByGenres(Long genreId) {
        return 0;
    }

}