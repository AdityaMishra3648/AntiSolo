package com.AntiSolo.AntiSolo.Repository;

import com.AntiSolo.AntiSolo.Entity.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepo extends MongoRepository<Project, ObjectId> {
}
