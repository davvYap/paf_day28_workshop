package sg.edu.nus.iss.workshop28.model;

import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

public class Review {
    public String _id;
    private int gid;
    private String name;
    private int rating;
    private String user;
    private String comment;
    private String cid;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public static Review createFromDocument(Document d) {
        Review r = new Review();
        r.set_id(d.getObjectId("_id").toString());
        r.setCid(d.getString("c_id"));
        r.setGid(d.getInteger("gid"));
        r.setUser(d.getString("user"));
        r.setRating(d.getInteger("rating"));
        r.setComment(d.getString("c_text"));
        List<String> gameName = (List<String>) d.get("game_name", List.class);
        r.setName(gameName.get(0));
        return r;
    }

    public JsonObjectBuilder toJSONObjectBuilder() {

        return Json.createObjectBuilder()
                .add("_id", this._id)
                .add("gid", this.gid)
                .add("name", this.name)
                .add("rating", this.rating)
                .add("user", this.user)
                .add("comment", this.comment)
                .add("cid", this.cid);
    }

}
