package sg.edu.nus.iss.workshop28.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation.AddFieldsOperationBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.workshop28.model.Game;

@Repository
public class BoardGameRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public Optional<Game> aggregateGameReviews(String gameId) {
        MatchOperation matchGameId = Aggregation.match(
                Criteria.where("gid").is(Integer.parseInt(gameId)));

        LookupOperation lookupOperation = Aggregation.lookup("reviews", "gid", "gid", "reviewDocs");

        ProjectionOperation projectionOperation = Aggregation
                .project("_id", "gid", "name", "year", "ranking", "users_rated", "url", "image")
                .and("reviewDocs._id").as("reviews");

        AddFieldsOperationBuilder aBuilder = Aggregation.addFields();
        aBuilder.addFieldWithValue("timestamp", LocalDateTime.now());
        AddFieldsOperation addFieldsOperation = aBuilder.build();

        // sequence of the operations is crucial
        Aggregation pipeline = Aggregation.newAggregation(matchGameId, lookupOperation, projectionOperation,
                addFieldsOperation);

        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "games", Document.class);

        if (!results.iterator().hasNext()) {
            return Optional.empty();
        }
        Document d = results.iterator().next();
        Game g = Game.createFromDocument(d);
        return Optional.of(g);
    }

    public Optional<List<Document>> getGamesReviewByUserRating(String user, int limit, String rating) {
        MatchOperation matchOperation = null;
        if (rating.equalsIgnoreCase("highest")) {
            matchOperation = Aggregation.match(Criteria.where("user").regex(user).and("rating").gte(5));
        } else {
            matchOperation = Aggregation.match(Criteria.where("user").regex(user).and("rating").lt(5));
        }

        LookupOperation lookupOperation = Aggregation.lookup("games", "gid", "gid", "gameReviews");

        ProjectionOperation projectionOperation = Aggregation.project("_id", "c_id", "user", "rating", "c_text", "gid")
                .and("gameReviews.name").as("game_name");

        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "rating");

        LimitOperation limitOperation = Aggregation.limit(limit);

        Aggregation pipeline = Aggregation.newAggregation(matchOperation, lookupOperation, projectionOperation,
                sortOperation, limitOperation);

        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "reviews", Document.class);
        List<Document> docs = results.getMappedResults();
        return Optional.of(docs);
    }

}
