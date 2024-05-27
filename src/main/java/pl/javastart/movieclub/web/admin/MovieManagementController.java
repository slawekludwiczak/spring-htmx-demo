package pl.javastart.movieclub.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.javastart.movieclub.domain.genre.GenreService;
import pl.javastart.movieclub.domain.genre.dto.GenreDto;
import pl.javastart.movieclub.domain.movie.MovieService;
import pl.javastart.movieclub.domain.movie.dto.MovieDto;
import pl.javastart.movieclub.domain.movie.dto.MovieSaveDto;

import java.util.List;

@Controller
public class MovieManagementController {
    private final MovieService movieService;
    private final GenreService genreService;

    public MovieManagementController(MovieService movieService, GenreService genreService) {
        this.movieService = movieService;
        this.genreService = genreService;
    }

    @GetMapping("/admin/dodaj-film")
    public String addMovieForm(Model model) {
        List<GenreDto> allGenres = genreService.findAllGenres();
        model.addAttribute("genres", allGenres);
        MovieSaveDto movie = new MovieSaveDto();
        model.addAttribute("movie", movie);
        return "admin/movie-form";
    }

    @PostMapping("/admin/zapisz-film")
    public String addMovie(MovieSaveDto movie, RedirectAttributes redirectAttributes) {
        movieService.saveMovie(movie);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Film %s został zapisany".formatted(movie.getTitle()));
        return "redirect:/admin";
    }

    @GetMapping("/admin/edytuj-film")
    public String editMovieForm(@RequestParam("movieId") Long movieId, Model model) {
        List<GenreDto> allGenres = genreService.findAllGenres();
        model.addAttribute("genres", allGenres);
        MovieDto movieToUpdate = movieService.findMovieById(movieId).orElseThrow();
        model.addAttribute("movie", movieToUpdate);
        return "admin/movie-form";
    }
}
