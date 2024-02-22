package com.example.commentservice.repository;

import com.example.commentservice.Entity.ParentChildComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentChildCommentRepo extends CrudRepository<ParentChildComment, Integer> {
    List<ParentChildComment> findByChildCommentId(Long parentCommentId);
}
