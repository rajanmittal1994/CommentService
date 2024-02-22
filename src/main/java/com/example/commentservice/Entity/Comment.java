package com.example.commentservice.Entity;

import com.example.commentservice.model.CommentDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "comment")
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=false)
    private Post post;

    private String content;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    private Long parentCommentId;

    private Long createdAt;

    public Comment(CommentDto commentDto, User user, Post post, Long parentCommentId) {
        this.post = post;
        this.content = commentDto.getContent();
        this.createdAt = System.currentTimeMillis();
        this.parentCommentId = parentCommentId;
        this.user =  user;
    }

}
