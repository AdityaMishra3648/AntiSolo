package com.AntiSolo.AntiSolo.Repository;

import com.AntiSolo.AntiSolo.Entity.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, ObjectId> {
    Page<Notification> findByParent(String parent, Pageable pageable);
}
