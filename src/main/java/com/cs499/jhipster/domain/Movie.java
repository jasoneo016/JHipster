package com.cs499.jhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private String year;

    @Column(name = "length")
    private String length;

    @Column(name = "genre")
    private String genre;

    @OneToOne
    @JoinColumn(unique = true)
    private Director director;

    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    private Set<Actor> actors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Movie title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public Movie year(String year) {
        this.year = year;
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLength() {
        return length;
    }

    public Movie length(String length) {
        this.length = length;
        return this;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getGenre() {
        return genre;
    }

    public Movie genre(String genre) {
        this.genre = genre;
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Director getDirector() {
        return director;
    }

    public Movie director(Director director) {
        this.director = director;
        return this;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public Movie actors(Set<Actor> actors) {
        this.actors = actors;
        return this;
    }

    public Movie addActor(Actor actor) {
        actors.add(actor);
        actor.setMovie(this);
        return this;
    }

    public Movie removeActor(Actor actor) {
        actors.remove(actor);
        actor.setMovie(null);
        return this;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        if (movie.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Movie{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", year='" + year + "'" +
            ", length='" + length + "'" +
            ", genre='" + genre + "'" +
            '}';
    }
}
