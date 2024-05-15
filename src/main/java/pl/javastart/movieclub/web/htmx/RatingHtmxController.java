package pl.javastart.movieclub.web.htmx;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.javastart.movieclub.domain.rating.Rating;
import pl.javastart.movieclub.domain.rating.RatingService;

@Controller
public class RatingHtmxController {
    private final RatingService ratingService;

    public RatingHtmxController(RatingService ratingService) {
        this.ratingService = ratingService;
    }
    
    @PostMapping("/api/ratings")
    public String addMovieRating(@RequestParam long movieId,
                                 @RequestParam int rating,
                                 Model model,
                                 Authentication authentication) throws InterruptedException {
        String currentUserEmail = authentication.getName();
        Rating savedRating = ratingService.addOrUpdateRating(currentUserEmail, movieId, rating);
        model.addAttribute("userRating", savedRating.getRating());
        Thread.sleep(2000);
        return "movie::movie-rating-buttons";
    }
}
