package com.cs499.jhipster.repository;

import com.cs499.jhipster.domain.Movie;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Movie entity.
 */
@SuppressWarnings("unused")
public interface MovieRepository extends JpaRepository<Movie,Long> {

}
