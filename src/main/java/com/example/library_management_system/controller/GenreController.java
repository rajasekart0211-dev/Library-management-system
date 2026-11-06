package com.example.library_management_system.controller;

import com.example.library_management_system.Exception.GenreException;
import com.example.library_management_system.payload.dto.GenreDTO;
import com.example.library_management_system.payload.response.ApiResponse;
import com.example.library_management_system.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genre")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreDTO> addGenre(@RequestBody GenreDTO genreDto){
        GenreDTO createdGenre = genreService.createGenre(genreDto);
        return ResponseEntity.ok().body(createdGenre);
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres(){
        return ResponseEntity.ok().body(
                genreService.getAllGenres()
        );
    }

    @GetMapping
    public ResponseEntity<GenreDTO> getGenresById(@RequestParam Long genreId) throws GenreException {
        return ResponseEntity.ok().body(
                genreService.getGenreById(genreId)
        );
    }

    @PutMapping("/{genreId}")
    public ResponseEntity<GenreDTO> updategenre(
            @RequestParam("genreId") Long genreId,
            @RequestBody GenreDTO genreDTO) throws GenreException {
        GenreDTO updatedDto = genreService.updateGenre(genreId, genreDTO);
        return ResponseEntity.ok().body(updatedDto);
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<?> deleteGenre(
            @PathVariable @RequestParam("/genreId") Long genreId
    ) throws GenreException {
        genreService.deleteGenreById(genreId);
        return ResponseEntity.ok().body(
                new ApiResponse("Genre Deleted - Soft Delete", true)
        );
    }

    @DeleteMapping("/{genreId}/hard")
    public ResponseEntity<?> hardDeleteGenre(
            @PathVariable @RequestParam("/genreId") Long genreId
    ) throws GenreException {
        genreService.hardDeleteGenre(genreId);
        return ResponseEntity.ok().body(
                new ApiResponse("Genre Deleted - Hard Delete", true)
        );
    }

    @GetMapping("/top-level")
    public ResponseEntity<List<GenreDTO>> getTopLevelGenres(){
        return ResponseEntity.ok().body(
                genreService.getTopLevelGenres()
        );
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalActiveGenres(){
        return ResponseEntity.ok().body(
                genreService.getTotalActiveGenres()
        );
    }

    @GetMapping("/{id}/book-count")
    public ResponseEntity<Long> getBookCountByGenres(
            @RequestParam("/id") Long genreId){
        return ResponseEntity.ok().body(
                genreService.getBookCountByGenres(genreId)
        );
    }


}
