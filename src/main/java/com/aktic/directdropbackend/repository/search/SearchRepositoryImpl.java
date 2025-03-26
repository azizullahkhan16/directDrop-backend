package com.aktic.directdropbackend.repository.search;

import com.aktic.directdropbackend.model.entity.Message;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepository{

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Message> fullTextSearchByChatRoom(Long chatRoomId, String keyword, String username, Pageable pageable) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("messages");
        List<Bson> pipeline = new ArrayList<>();

        // Stage 1: Match chatRoomId
        if (chatRoomId != null) {
            pipeline.add(Aggregates.match(Filters.eq("chatRoom.room_id", chatRoomId)));
        }

        // Stage 2: Full-text search
        if (keyword != null && !keyword.trim().isEmpty()) {
            pipeline.add(Aggregates.match(Filters.text(keyword)));
        }

        // Stage 3: Match sender/receivers by username
        if (username != null && !username.trim().isEmpty()) {
            Bson regexFilter = Filters.regex("sender.username", username, "i");
            Bson receiversFilter = Filters.elemMatch("receivers", Filters.regex("username", username, "i"));
            pipeline.add(Aggregates.match(Filters.or(regexFilter, receiversFilter)));
        }

        // Stage 4: Sorting
        pipeline.add(Aggregates.sort(Sorts.descending("createdAt")));

        // Stage 5: Pagination
        pipeline.add(Aggregates.skip((int) pageable.getOffset()));
        pipeline.add(Aggregates.limit(pageable.getPageSize()));

        // Execute aggregation
        AggregateIterable<Document> results = collection.aggregate(pipeline, Document.class);

        // Convert results
        List<Message> messages = new ArrayList<>();
        for (Document doc : results) {
            Message message = mongoTemplate.getConverter().read(Message.class, doc);
            messages.add(message);
        }

        // Get total count separately
        long total = collection.countDocuments(Filters.eq("chatRoom.roomId", chatRoomId));

        return new PageImpl<>(messages, pageable, total);
    }


    @Override
    public Page<Message> fullTextSearch(String keyword, String username, Pageable pageable) {
        return fullTextSearchByChatRoom(null, keyword, username, pageable);
    }
}
