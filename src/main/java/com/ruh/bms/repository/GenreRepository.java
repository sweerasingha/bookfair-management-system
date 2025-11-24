package com.ruh.bms.repository;

import com.ruh.bms.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);

    List<Genre> findByActiveTrue();

    Boolean existsByName(String name);
}
