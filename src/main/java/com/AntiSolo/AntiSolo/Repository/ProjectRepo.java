package com.AntiSolo.AntiSolo.Repository;

import com.AntiSolo.AntiSolo.Entity.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectRepo extends MongoRepository<Project, ObjectId> {
    @Aggregation(pipeline = {
            "{ $match: { _id: { $nin: ?0 } } }", // Exclude already fetched projects
            "{ $sample: { size: ?1 } }"
    })
    List<Project> findRandomProjectsExcluding(List<ObjectId> excludedIds, int limit);

    @Aggregation(pipeline = {
            "{ $match: { _id: { $nin: ?0 }, tags: { $in: ?1 } } }",
            "{ $sample: { size: ?2 } }"
    })
    List<Project> findRandomProjectsExcludingWithTags(List<ObjectId> excludedIds, List<String> tags, int limit);
}
