package com.AntiSolo.AntiSolo.Repository;

import com.AntiSolo.AntiSolo.Entity.ReportEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReportRepo extends MongoRepository<ReportEntity,ObjectId> {

    Optional<ReportEntity> findByProjectId(String projectId);

}

