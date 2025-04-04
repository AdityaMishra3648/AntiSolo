package com.AntiSolo.AntiSolo.Repository;

import com.AntiSolo.AntiSolo.Entity.ProjectChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface GroupChatsRepo extends MongoRepository<ProjectChatMessage,String> {

    List<ProjectChatMessage> findByProjectIdOrderByCreatedAtDesc(String id, Pageable pageable);

    // Subsequent pages: Fetch messages created before `cursorCreatedAt` (older messages)
    List<ProjectChatMessage> findByProjectIdAndCreatedAtBeforeOrderByCreatedAtDesc(String id, Instant cursorCreatedAt, Pageable pageable);

}
