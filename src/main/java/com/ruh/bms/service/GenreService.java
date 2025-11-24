package com.ruh.bms.service;

import com.ruh.bms.model.Genre;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GenreService {
    @Transactional(readOnly = true)
    List<Genre> getAllGenres();

    @Transactional(readOnly = true)
    List<Genre> getActiveGenres();

    @Transactional(readOnly = true)
    Genre getGenreById(Long id);

    @Transactional
    Genre createGenre(Genre genre);

    @Transactional
    Genre updateGenre(Long id, Genre genreDetails);

    @Transactional
    void deleteGenre(Long id);
}
