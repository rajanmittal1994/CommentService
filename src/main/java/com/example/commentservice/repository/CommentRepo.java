package com.example.commentservice.repository;

import com.example.commentservice.Entity.Comment;
import com.example.commentservice.Entity.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends CrudRepository<Comment, Long> {

    @Query(nativeQuery = true, value = "select * from comment where post_id= ?1 and  parent_comment_id = -1 and id > ?2 limit ?3")
    List<Comment> findCommentByPost(Long postId, Long offset, Integer limit);

    @Query(nativeQuery = true, value = "select * from comment where parent_comment_id= ?1 and id > ?2 limit ?3")
    List<Comment> findCommentByParentCommentId(Long commentId, Long offset, Integer limit);

    List<Comment> findCommentByParentCommentIdIn(List<Long> commentId);
}
