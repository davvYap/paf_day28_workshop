use boardgames;

// task a
db.games.aggregate([
    {
        $match: {gid:3}
    },
    {
        $lookup: {
            from: "reviews",
            localField: "gid",
            foreignField: "gid",
            as: "reviewsDocs"
        }
    },
    {
        $project: { 
            _id: -1, 
            gid: 1,
            name: 1,
            year: 1,
            ranking: 1,
            users_rated: 1,
            url: 1,
            image: 1,
            reviews: "$reviewsDocs._id",
            timestamp: "$$NOW" 
        }
    }
]);

// task b
db.reviews.aggregate([
    {
        $match: {$and: [
            {user: "desertfox2004"},
            {rating: {$gt: 5}}
        ]}
    },
    {
        $lookup: {
            from: "games",
            localField: "gid",
            foreignField: "gid",
            as: "gameReviews"
        }
    },
    {
        $project: { 
            _id: 1, 
            c_id: 1, 
            user: 1, 
            rating: 1, 
            c_text: 1, 
            gid: 1,
            game_name: "$gameReviews.name"
        }
    },
    { 
        $limit: 3
    }
]);

// to find user with most reviews
db.reviews.aggregate([
    {
        $group: {
            _id: "$user",
            count: {$sum: 1},
            gid: {$push: "$gid"}
        }
    },
    {
        $sort: {count: -1}
    }
]);

