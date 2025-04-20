package com.AntiSolo.AntiSolo.Repository;

import com.AntiSolo.AntiSolo.Entity.ReportEntity;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepo extends MongoRepository<ReportEntity,ObjectId> {

    Optional<ReportEntity> findByProjectId(String projectId);
    List<ReportEntity> findAllByOrderByCreatedAtDesc();
    Page<ReportEntity> findByTypeOrderByCreatedAtDesc(int type, Pageable pageable);

}

