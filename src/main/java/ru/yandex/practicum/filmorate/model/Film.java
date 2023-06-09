package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class Film {

    private int id;
    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;

    private Set<Genre> genres = new HashSet<>();

    private Set<Integer> likes = new HashSet<>();

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

}
