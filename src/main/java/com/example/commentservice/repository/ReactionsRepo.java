package com.example.commentservice.repository;

import com.example.commentservice.Entity.Reactions;
import com.example.commentservice.model.ContentType;
import com.example.commentservice.model.Reaction;
import com.example.commentservice.model.ReactionDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReactionsRepo extends CrudRepository<Reactions, Long> {
    Reactions findByContentTypeIdAndContentTypeAndUserIdAndReaction(Long contentTypeId, ContentType contentType,
                                                                    Integer userId, Reaction reaction);

    @Query(nativeQuery = true, value = "select * from reactions where content_type_id= ?1 and content_type = ?2 " +
            "and id > ?4 and reaction = ?3 limit ?5")
    List<Reactions> findByContentTypeIdAndContentType(Long contentId, ContentType contentType, Reaction reaction,
                                                      Long offset, Integer limit);
}
