package com.ruh.bms.service;

import com.ruh.bms.exception.BadRequestException;
import com.ruh.bms.exception.ResourceNotFoundException;
import com.ruh.bms.model.Genre;
import com.ruh.bms.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> getActiveGenres() {
        return genreRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", "id", id));
    }

    @Transactional
    @Override
    public Genre createGenre(Genre genre) {
        if (genreRepository.existsByName(genre.getName())) {
            throw new BadRequestException("Genre with name '" + genre.getName() + "' already exists");
        }
        Genre savedGenre = genreRepository.save(genre);
        log.info("Genre created: {}", savedGenre.getName());
        return savedGenre;
    }

    @Transactional
    @Override
    public Genre updateGenre(Long id, Genre genreDetails) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", "id", id));

        // Check if new name already exists (excluding current genre)
        if (!genre.getName().equals(genreDetails.getName()) &&
                genreRepository.existsByName(genreDetails.getName())) {
            throw new BadRequestException("Genre with name '" + genreDetails.getName() + "' already exists");
        }

        genre.setName(genreDetails.getName());
        genre.setDescription(genreDetails.getDescription());
        genre.setActive(genreDetails.getActive());

        Genre updatedGenre = genreRepository.save(genre);
        log.info("Genre updated: {}", updatedGenre.getName());
        return updatedGenre;
    }

    @Transactional
    @Override
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", "id", id));
        genreRepository.delete(genre);
        log.info("Genre deleted: {}", genre.getName());
    }
}
