package com.example.commentservice.Entity;

import com.example.commentservice.exception.CommentServiceException;
import com.example.commentservice.model.ContentType;
import com.example.commentservice.model.ReactionDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uniqueContentTypeAndId", columnNames = {"contentType", "contentTypeId"})
})
public class ReactionCount {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private ContentType contentType;

    private Long contentTypeId;

    @Column(name = "_likeCount")
    private Integer likeCount;

    private Integer disLikeCount;

    private Long createdAt;

    private Long updatedAt;

    public ReactionCount(ReactionDto reactionDto) {
        this.contentType = reactionDto.getContentType();
        this.contentTypeId = reactionDto.getId();
        this.likeCount = 0;
        this.disLikeCount = 0;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public void incrementDisLikeCount() {
        this.disLikeCount++;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementDisLikeCount() throws CommentServiceException {
        if(this.disLikeCount == 0) {
            throw new CommentServiceException("disLikeCount is already zero. Post/Comment's reaction count can not reduced further.");
        }
        this.disLikeCount--;
    }

    public void decrementLikeCount() throws CommentServiceException {
        if(this.likeCount == 0) {
            throw new CommentServiceException("likeCount is already zero. Post/Comment's reaction count can not reduced further.");
        }
        this.likeCount--;
    }
}
