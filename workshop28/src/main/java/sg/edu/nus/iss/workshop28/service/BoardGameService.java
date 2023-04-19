package sg.edu.nus.iss.workshop28.service;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.workshop28.model.Game;
import sg.edu.nus.iss.workshop28.model.Review;
import sg.edu.nus.iss.workshop28.repository.BoardGameRepository;

@Service
public class BoardGameService {

    @Autowired
    BoardGameRepository boardGameRepository;

    public Game aggregateGameReviews(String gameId) {
        Optional<Game> game = boardGameRepository.aggregateGameReviews(gameId);
        if (game.isEmpty()) {
            return null;
        }
        return game.get();
    }

    public List<Review> getGamesReviewByUserRating(String user, int limit, String rating) {

        Optional<List<Document>> docs = boardGameRepository.getGamesReviewByUserRating(user, limit, rating);
        if (docs.isEmpty()) {
            return null;
        }
        List<Document> documents = docs.get();
        List<Review> reviews = documents.stream()
                .map(doc -> Review.createFromDocument(doc))
                .toList();
        return reviews;
    }

}
