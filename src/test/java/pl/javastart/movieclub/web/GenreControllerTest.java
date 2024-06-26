package pl.javastart.movieclub.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.javastart.movieclub.domain.genre.GenreService;
import pl.javastart.movieclub.domain.genre.dto.GenreDto;
import pl.javastart.movieclub.domain.movie.MovieService;
import pl.javastart.movieclub.domain.movie.dto.MovieDto;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
class GenreControllerTest extends BaseWebTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GenreService genreService;
    @MockBean
    MovieService movieService;

    @Test
    void shouldFindGenre() throws Exception {
        GenreDto genre = new GenreDto(1L, "Horror", "Straszne filmy");
        MovieDto movie1 = new MovieDto(
                1L,
                "Laleczka Chucky",
                "Chucky",
                "Bardzo straszny",
                "Bardzo bardzo straszny film",
                "abc123",
                1987,
                "Horror",
                true,
                "poster.png",
                8.8,
                123);
        List<MovieDto> horrorMovies = List.of(movie1);
        when(genreService.findGenreByName("horror")).thenReturn(Optional.of(genre));
        when(movieService.findMoviesByGenreName("horror")).thenReturn(horrorMovies);
        mockMvc.perform(get("/gatunek/horror"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-listing"))
                .andExpect(model().attribute("heading", genre.getName()))
                .andExpect(model().attribute("description", genre.getDescription()))
                .andExpect(model().attribute("movies", horrorMovies));
    }

    @Test
    void shouldNotFindGenre() throws Exception {
        mockMvc.perform(get("/gatunek/horror"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindThreeGenres() throws Exception {
        GenreDto genre1 = new GenreDto(1L, "Horror", "Straszne filmy");
        GenreDto genre2 = new GenreDto(2L, "Komedia", "Śmieszne filmy");
        List<GenreDto> genres = List.of(genre1, genre2);
        when(genreService.findAllGenres()).thenReturn(genres);
        mockMvc.perform(get("/gatunki-filmowe"))
                .andExpect(status().isOk())
                .andExpect(view().name("genre-listing"))
                .andExpect(model().attribute("genres", genres));
    }
}
