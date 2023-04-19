package sg.edu.nus.iss.workshop28.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import sg.edu.nus.iss.workshop28.model.Game;
import sg.edu.nus.iss.workshop28.model.Review;
import sg.edu.nus.iss.workshop28.model.UserReview;
import sg.edu.nus.iss.workshop28.service.BoardGameService;

@RestController
public class BoardGameController {

    @Autowired
    BoardGameService boardGameService;

    @GetMapping(path = "/game/{game_id}/reviews")
    public ResponseEntity<String> getGameReviews(@PathVariable String game_id) {
        Game game = boardGameService.aggregateGameReviews(game_id);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Error", "Game with %s not found".formatted(game_id))
                            .build().toString());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(game.toJSON().toString());

    }

    @GetMapping(path = "/games/{rating}")
    public ResponseEntity<String> getUserGameReviews(@PathVariable String rating, @RequestParam String user,
            @RequestParam String limit) {
        List<Review> reviews = boardGameService.getGamesReviewByUserRating(user, Integer.parseInt(limit), rating);
        UserReview userReview = new UserReview(rating, reviews);
        if (reviews == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Error", "User %s not found".formatted(user))
                            .build().toString());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userReview.toJSON().toString());
    }

}
