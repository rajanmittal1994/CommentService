package com.example.commentservice.model;

import com.example.commentservice.Entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private Long createdAt;
    private Long parentCommentId;
    private Long postId;
    private String content;
    private Map<Reaction,Integer> reactionMap;
    private Integer userId;
    boolean replyExist;

    public CommentDto(Comment comment, Long parentCommentId, Map<Reaction,Integer> reactionMap) {
        this.id = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.userId = comment.getUser().getId();
        this.parentCommentId = parentCommentId;
        this.reactionMap = reactionMap;
    }

}
