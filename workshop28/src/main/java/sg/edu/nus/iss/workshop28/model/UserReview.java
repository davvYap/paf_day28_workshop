package sg.edu.nus.iss.workshop28.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class UserReview {
    private String rating;
    private List<Review> reviews = new ArrayList<>();
    private LocalDateTime timestamp;

    public UserReview() {
    }

    public UserReview(String rating, List<Review> reviews) {
        this.rating = rating;
        this.reviews = reviews;
        this.timestamp = LocalDateTime.now();
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public JsonObject toJSON() {
        JsonArrayBuilder jsArr = Json.createArrayBuilder();
        for (Review review : reviews) {
            jsArr.add(review.toJSONObjectBuilder());
        }
        return Json.createObjectBuilder()
                .add("rating", this.rating)
                .add("games", jsArr)
                .add("timestamp", this.timestamp.toString())
                .build();
    }

}
