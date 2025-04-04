package com.AntiSolo.AntiSolo.Repository;
import com.AntiSolo.AntiSolo.Entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    // First page: Fetch latest messages (sorted by createdAt descending)
    List<ChatMessage> findByChatIdOrderByCreatedAtDesc(String id, Pageable pageable);

    // Subsequent pages: Fetch messages created before `cursorCreatedAt` (older messages)
    List<ChatMessage> findByChatIdAndCreatedAtBeforeOrderByCreatedAtDesc(String id, Instant cursorCreatedAt, Pageable pageable);
}
