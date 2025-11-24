package com.ruh.bms.controller;

import com.ruh.bms.dto.ApiResponse;
import com.ruh.bms.model.Genre;
import com.ruh.bms.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreServiceImpl;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllGenres() {
        List<Genre> genres = genreServiceImpl.getAllGenres();
        return ResponseEntity.ok(
                new ApiResponse(true, "Genres retrieved successfully", genres)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse> getActiveGenres() {
        List<Genre> genres = genreServiceImpl.getActiveGenres();
        return ResponseEntity.ok(
                new ApiResponse(true, "Active genres retrieved successfully", genres)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getGenreById(@PathVariable Long id) {
        Genre genre = genreServiceImpl.getGenreById(id);
        return ResponseEntity.ok(
                new ApiResponse(true, "Genre retrieved successfully", genre)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> createGenre(@Valid @RequestBody Genre genre) {
        Genre createdGenre = genreServiceImpl.createGenre(genre);
        return new ResponseEntity<>(
                new ApiResponse(true, "Genre created successfully", createdGenre),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> updateGenre(@PathVariable Long id, @Valid @RequestBody Genre genre) {
        Genre updatedGenre = genreServiceImpl.updateGenre(id, genre);
        return ResponseEntity.ok(
                new ApiResponse(true, "Genre updated successfully", updatedGenre)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> deleteGenre(@PathVariable Long id) {
        genreServiceImpl.deleteGenre(id);
        return ResponseEntity.ok(
                new ApiResponse(true, "Genre deleted successfully")
        );
    }
}
