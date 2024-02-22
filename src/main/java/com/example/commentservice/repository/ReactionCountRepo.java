package com.example.commentservice.repository;

import com.example.commentservice.Entity.ReactionCount;
import com.example.commentservice.model.ContentType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReactionCountRepo extends CrudRepository<ReactionCount, Long> {
    ReactionCount findByContentTypeIdAndContentType(Long commentId, ContentType contentType);
    List<ReactionCount> findByContentTypeIdInAndContentType(List<Long> idList, ContentType contentType);
}
