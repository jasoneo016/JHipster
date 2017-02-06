package com.cs499.jhipster.repository;

import com.cs499.jhipster.domain.Director;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Director entity.
 */
@SuppressWarnings("unused")
public interface DirectorRepository extends JpaRepository<Director,Long> {

}
