package sg.edu.nus.iss.workshop28.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Game implements Serializable {
    private int gid;
    private String name;
    private int year;
    private int ranking;
    private int users_rated;
    private String url;
    private String image;
    private LocalDateTime timestamp;
    private List<String> reviews;

    public Game() {
    }

    public Game(int gid, String name, int year, int ranking, int users_rated, String url, String image,
            LocalDateTime timestamp, List<String> reviews) {
        this.gid = gid;
        this.name = name;
        this.year = year;
        this.ranking = ranking;
        this.users_rated = users_rated;
        this.url = url;
        this.image = image;
        this.timestamp = timestamp;
        this.reviews = reviews;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getUsers_rated() {
        return users_rated;
    }

    public void setUsers_rated(int users_rated) {
        this.users_rated = users_rated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public static Game createFromDocument(Document d) {
        Game game = new Game();
        game.setGid(d.getInteger("gid"));
        game.setName(d.getString("name"));
        game.setYear(d.getInteger("year"));
        game.setRanking(d.getInteger("ranking"));
        game.setUsers_rated(d.getInteger("users_rated"));
        game.setUrl(d.getString("url"));
        game.setImage(d.getString("image"));
        game.setTimestamp(LocalDateTime.now());

        List<Object> reviews = (ArrayList<Object>) d.get("reviews", List.class);
        List<String> reviewsUrl = new ArrayList<>();
        for (Object object : reviews) {
            ObjectId id = (ObjectId) object;
            reviewsUrl.add("/review/" + id.toString());
        }
        game.setReviews(reviewsUrl);

        return game;
    }

    public JsonObject toJSON() {
        JsonArrayBuilder jsArr = Json.createArrayBuilder();
        for (String string : reviews) {
            jsArr.add(string);
        }
        return Json.createObjectBuilder()
                .add("gid", this.gid)
                .add("name", this.name)
                .add("year", this.year)
                .add("rank", this.ranking)
                .add("users-rated", this.users_rated)
                .add("url", this.url)
                .add("image", this.image)
                .add("reviews", jsArr)
                .add("timestamp", this.timestamp.toString())
                .build();
    }

    public JsonObjectBuilder toJSONBuilder() {
        JsonArrayBuilder jsArr = Json.createArrayBuilder();
        for (String string : reviews) {
            jsArr.add(string);
        }
        return Json.createObjectBuilder()
                .add("gid", this.gid)
                .add("name", this.name)
                .add("year", this.year)
                .add("rank", this.ranking)
                .add("users-rated", this.users_rated)
                .add("url", this.url)
                .add("image", this.image)
                .add("reviews", jsArr)
                .add("timestamp", this.timestamp.toString());
    }

}
